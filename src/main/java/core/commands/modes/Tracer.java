package core.commands.modes;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.Command;
import core.commands.Helpable;
import core.commands.Mode;
import core.modules.Time;
import core.modules.session.SessionManager;
import core.modules.session.UserIOStream;
import core.modules.tracer.CustomCLI;
import ru.ifmo.cs.bcomp.MicroPrograms;
import vk.VKManager;

import java.util.Calendar;
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
        boolean cut = false;
        String oldOutput = "";
        if (output.available()){
            oldOutput = output.readString() + "\n";
        }
        String userInput;
        if (checkCut(message.getBody())){
            userInput=message.getBody().replace("@cut", "");
            cut = true;
        }
        else userInput = message.getBody();

        input.writeln(userInput);

        while(true){
            if (output.available()){
                String res = output.readString();
                if (res.replace("\n", "").equals("exit")){
                    SessionManager.deleteSession(message.getUserId());
                    return "Вы закончили сессию трассировки";
                }
                //return oldOutput + res;
                String response = oldOutput;
                if (cut){
                    for (String line : res.split("\n")){
                        String[] regs = line.split(" ");
                        if (regs.length > 1){
                            if (regs[1].equals("F000")){
                                response += "\n" + line;
                                break;
                            }
                            else {
                                response += "\n" + line;
                            }
                        }
                    }
                } else response += res;

                return response.replace(" ", "&#4448;");
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
        return msg.matches(".*@cut.*");
    }
}
