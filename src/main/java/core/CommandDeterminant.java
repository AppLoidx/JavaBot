package core;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.Command;
import core.commands.Unknown;
import core.commands.VKCommands.VKCommand;

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

    public static VKCommand getVKCommand(ArrayList<Command> commands, Message message) {
        String body = message.getBody();

        for (Command command : commands
        ) {
            if (command instanceof VKCommand) {
                if (command.getName().equals(body.split(" ")[0])) {
                    return (VKCommand) command;
                }
            }
        }

        return new Unknown();
    }

}
