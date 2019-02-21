package core;

import core.commands.CommandDeterminant;
import core.commands.CommandManager;

public class Commander {

    public static String getResponse(String userInput){
        return CommandDeterminant.getCommand(CommandManager.getCommands(), userInput).init(userInput);
    }

}
