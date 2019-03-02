package core;

import com.vk.api.sdk.objects.messages.Message;

public class Commander {

    public static String getResponse(String userInput){
        return CommandDeterminant.getCommand(CommandManager.getCommands(), userInput).init(userInput);
    }

    public static String getResponse(Message message){
        return CommandDeterminant.getVKCommand(CommandManager.getCommands(), message).exec(message);
    }
    public static void main(String[] args) {
        System.out.println(Commander.getResponse("user -l marshall -p myLongPass"));
    }

}
