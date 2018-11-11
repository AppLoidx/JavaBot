package core.commands;

import core.commands.enums.CommandEnum;

/**
 * Определяет перечисление команды
 *
 * @author Артур Куприянов
 * @version 1.0.0
 */
public class CommandDeterminant {
    /**
     * По команде определяем его принадлежность к перечислению команд
     * @param command обрабатываемая команда
     * @return перечисление к которому принадлежит команда
     */
    public static CommandEnum readCommand(String command){
        command = command.split(" ")[0];

        switch (command){
            case "schedule":
                return CommandEnum.SCHEDULE;
            default:
                    return CommandEnum.UNKNOWN;
        }
    }

    /**
     * По значению перечисления возвращаем команду
     * @param commandEnum значение перечисления команды
     * @return возвращает объект команды
     */
    public static Command getCommand(CommandEnum commandEnum){
        switch(commandEnum){
            case SCHEDULE:
                return new Schedule();
            default:
                return new Unknown();
        }
    }
}
