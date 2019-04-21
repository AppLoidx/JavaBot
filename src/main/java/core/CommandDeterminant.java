package core;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.Command;
import core.commands.ProgramSpecification;
import core.commands.TelegramCommand;
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
            if (userInput.equals(command.getName())){
                return command;
            }
        }

        return new Unknown();
    }

    public static VKCommand getVKCommand(ArrayList<Command> commands, Message message) {
        String body = message.getBody().toLowerCase();

        for (Command command : commands
        ) {
            if (command instanceof VKCommand) {
                if (body.split(" ")[0].equals(command.getName())) {
                    return (VKCommand) command;
                }
            }
        }

        return new Unknown();
    }
    public static TelegramCommand getTelegramCommand(ArrayList<Command> commands, Message message){
        String body = message.getBody().toLowerCase();

        for (Command command : commands
        ) {
            if (command instanceof VKCommand) {
                if (body.split(" ")[0].equals(command.getName())) {
                    return (TelegramCommand) command;
                }
            }
        }

        return new Unknown();
    }


    public static ProgramSpecification getProgramCommand(ArrayList<Command> commands, String message) {

        for (Command command : commands
        ) {
            if (command instanceof ProgramSpecification) {
                if (message.split(" ")[0].matches(command.getName())) {
                    return (ProgramSpecification) command;
                }
            }
        }

        return new Unknown();
    }

}
