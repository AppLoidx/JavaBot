package core;

import core.commands.CommandDeterminant;

public class Commander {

    public static String getResponse(String userInput){
        return CommandDeterminant.getCommand(userInput).init(userInput);
    }

}
