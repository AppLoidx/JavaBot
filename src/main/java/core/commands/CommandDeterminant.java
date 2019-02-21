package core.commands;

import java.util.ArrayList;

/**
 * Определяет команду
 *
 * @author Артур Куприянов
 * @version 1.1.0
 */
public class CommandDeterminant {

    /**
     * По значению перечисления возвращаем команду
     * @return возвращает объект команды
     */
    public static Command getCommand(ArrayList<Command> commands, String userInput){
        userInput = userInput.split(" ")[0];
        for (Command command: commands
             ) {
            if (command.getName().equals(userInput)){
                return command;
            }
        }

        return new Unknown();
    }
}
