package core.commands.modes.tracer;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.docs.Doc;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.docs.DocsGetMessagesUploadServerType;
import core.commands.Command;
import core.commands.Helpable;
import core.commands.Mode;
import core.modules.VKDocUploader;
import core.modules.session.SessionManager;
import core.modules.session.UserIOStream;
import core.modules.tracer.cli.CustomCLI;
import core.modules.tracer.cli.MnemonicInterpreter;
import ru.ifmo.cs.bcomp.MicroPrograms;
import vk.VKManager;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Arthur Kupriyanov
 */
public class Tracer extends Command implements Mode, Helpable {

    private UserIOStream input;
    private UserIOStream output;

    @Override
    protected void setConfig() {
        commandName = "tracer";
    }

    @Override
    public String getResponse(String input) {
        return null;
    }

    @Override
    public String getResponse(Message message) {

        String userInput = message.getBody();

        checkExit(message);

        // регистр срезки после команды HLT
        boolean cut = checkCut(userInput);

        // регистр для двойного ката (срезка после второго HLT)
        boolean doubleCut = doubleCutCheck(userInput);

        // регистр генерации xls-файла
        boolean generate = checkGenerate(userInput);

        // регистр печати мнемоники
        boolean mnemonic = checkMnemonic(userInput);

        String oldOutput = "";
        if (output.available()){
            oldOutput = output.readString() + "\n";
        }


        input.writeln(cleanMsg(userInput));

        while(true){
            if (output.available()){
                String res = output.readString();
                if (res.replace("\n", "").matches(".*!exit.*")){
                    exit(message.getUserId());
                    return "Вы закончили сессию трассировки";
                }

                StringBuilder response = new StringBuilder();
                if (cut){
                    cut(response, res, doubleCut);
                } else response.append(res);

//                if (mnemonic) response = insertMnemonic(response.toString());

                if (generate){
                    if (mnemonic) generateDoc(message, insertMnemonic(response.toString()).toString());
                    else generateDoc(message, response.toString());
                }
                return oldOutput + "\n" + response.toString().replace(" ", "&#4448;");
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onExit(){
        String resPath = "src/main/resources/xls";
        String[] files = new File(resPath).list();
        if (files!=null)
        for (String fileName: files){
            File file = new File(resPath+"/"+fileName);
            file.delete();
        }
    }

    @Override
    public void setOutput(UserIOStream output) {
        this.output = output;
    }

    @Override
    public void setInput(UserIOStream input) {
        this.input = input;
    }

    @Override
    public UserIOStream getInputStream() {
        return input;
    }

    @Override
    public UserIOStream getOutputStream() {
        return output;
    }

    @Override
    public void run() {
        try {
            CustomCLI cli = new CustomCLI(MicroPrograms.getMicroProgram("base"), output );
            cli.cli(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getManual() {
        String additional = "Базовый эмулятор БЭВМ, основанный эмуляторе bcomp\n" +
                "Подробнее : https://github.com/AppLoidx/JavaBot/wiki/Режим-tracer-%5Bдля-Session%5D\n";
        return additional + CustomCLI.getHelp();

    }

    @Override
    public String getDescription() {
        return "Эмулятор БЭВМ";
    }

    private boolean checkCut(String msg){
        return msg.matches(".*&cut.*");
    }
    private boolean doubleCutCheck(String msg) { return msg.matches(".*&cut2.*");}

    private boolean checkGenerate(String msg) { return msg.matches(".*&generate.*");}

    private String cleanMsg(String msg){
        Pattern p = Pattern.compile("\\[.*]");
        Matcher m = p.matcher(msg);

        while (m.find()){
            msg = msg.replace(m.group(), "");
        }

        m = Pattern.compile("&cut[2]*").matcher(msg);
        while (m.find()){
            msg = msg.replace(m.group(),"");
        }
        m = Pattern.compile("&generate").matcher(msg);
        while (m.find()){
            msg = msg.replace(m.group(),"");
        }
        m = Pattern.compile("&mnemonic").matcher(msg);
        while (m.find()){
            msg = msg.replace(m.group(),"");
        }
        return msg;
    }

    private void generateDoc(Message message, String response){
        boolean fail = false;
        String failMessage = "ошибок не найдено";
        File file = new TraceGenerator().generate(response, String.valueOf(message.getUserId()));
        if (file == null){
            failMessage = "Ошибка сервера при отправке файла: не найден целевой файл";
            fail = true;
        }
        try {
            if (!fail) {
                Doc doc = VKDocUploader.upload(message.getUserId(), file, DocsGetMessagesUploadServerType.DOC);
                file.deleteOnExit();
                if (doc == null) {
                    failMessage = "Не удалось загрузить файл на сервер вк";
                    fail = true;
                }else new VKManager().sendMessage(doc.getUrl(), message.getUserId());
            }
        } catch (ClientException | ApiException e) {
            failMessage = e.getMessage();
            fail = true;
            e.printStackTrace();
        }
        if (fail) {
            new VKManager().sendMessage(failMessage, message.getUserId());
        }
        if (!fail) file.delete();
    }

    private void cut(StringBuilder response, String res, boolean doubleCut){
        for (String line : res.split("\n")){
            String[] regs = line.split(" ");
            if (regs.length > 1){
                if (regs[1].equals("F000") && !doubleCut){
                    response.append("\n").append(line);
                    break;
                } else if (regs[1].equals("F000")){
                    doubleCut = false;
                    response.append("\n").append(line);
                }
                else {
                    response.append("\n").append(line);
                }
            }
        }
    }

    private StringBuilder insertMnemonic(String res){
        StringBuilder response = new StringBuilder();
        for (String line : res.split("\n")){
            String[] regs = line.split(" ");

            if (regs.length > 1){
                if (regs[0].matches("[0-9a-fA-F]{3}")){
                    int emptyFieldsCount = 10 - regs.length;
                    for (int i=0; i< emptyFieldsCount; i++){
                        line += " -";
                    }
                    response.append("\n").append(line).append(" ").append(MnemonicInterpreter.interpret(regs[1]));
                } else {
                    response.append("\n").append(line);
                }
            }
        }
        return response;
    }

    private void exit(int userID){
        SessionManager.deleteSession(userID);
        onExit();
    }

    private void checkExit(Message msg){
        if (msg.getBody().matches(".* exit .*")) exit(msg.getUserId());
    }

    private boolean checkMnemonic(String msg){
        return msg.matches(".*&mnemonic.*");
    }
}
