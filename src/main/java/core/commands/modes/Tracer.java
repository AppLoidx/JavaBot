package core.commands.modes;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.Command;
import core.commands.Helpable;
import core.commands.Mode;
import core.modules.session.SessionManager;
import core.modules.session.UserIOStream;
import core.modules.tracer.CustomCLI;
import ru.ifmo.cs.bcomp.MicroPrograms;
import vk.VKManager;

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
        String oldOutput = "";
        if (output.available()){
            oldOutput = output.readString() + "\n";
        }
        input.writeln(message.getBody());
        while(true){
            if (output.available()){
                String res = output.readString();
                if (res.replace("\n", "").equals("exit")){
                    SessionManager.deleteSession(message.getUserId());
                    return "Вы закончили сессию трассировки";
                }
                return oldOutput + res;
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
        return CustomCLI.getHelp();

    }

    @Override
    public String getDescription() {
        return "БЭВМ";
    }
}
