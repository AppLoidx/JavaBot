package core.commands;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.VKCommands.VKCommand;

public class NaturalLanguage implements VKCommand, ServiceCommand {

    public static String getNaturalResponse(Message message){
        String msgStr = message.getBody().toLowerCase();
        if (msgStr.matches("спасиб.*") || msgStr.equals("спс")){
            return "Всегда пожалуйста!";
        }
        if (msgStr.equals("да")) return "Хорошо!";

        return null;
    }

    @Override
    public void service() {

    }

    @Override
    public String exec(Message message) {
        return null;
    }
}