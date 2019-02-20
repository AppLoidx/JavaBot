package core.commands;


import java.util.ArrayList;

/**
 * Используется для распределения команд
 *
 * Понадобится при создании нескольких уровней доступа
 */
public class CommandManager {
    private static ArrayList<Command> commands = new ArrayList<>();

    static {
        commands.add(new Schedule());
        commands.add(new Link());
        commands.add(new Unknown());
        commands.add(new RegSimple());
        commands.add(new Note());
        commands.add(new Queue());
        commands.add(new Test());

    }

    public static ArrayList<Command> getCommands(){
        return commands;
    }
}
