package core;


import core.commands.*;
import core.commands.modes.Session;
import core.commands.modes.Tracer;
import core.commands.queue.Queue;
import core.modules.session.SessionManager;

import java.util.ArrayList;

/**
 * Используется для распределения команд
 *
 * Понадобится при создании нескольких уровней доступа
 * @author Arthur Kupriyanov
 * @version 1.0.1
 */
public class CommandManager {
    private static ArrayList<Command> commands = new ArrayList<>();

    static {
        commands.add(new Schedule());
        commands.add(new Queue());
        commands.add(new Reg());
        commands.add(new Help());
        commands.add(new Note());
        commands.add(new Version());
        commands.add(new Spam());
        commands.add(new Comment());
        commands.add(new Test());
        commands.add(new Quest());
        commands.add(new TimedCommand());
        commands.add(new News());
        commands.add(new Session());

        SessionManager.addMode(new Tracer());

    }

    public static ArrayList<Command> getCommands(){
        return commands;
    }
    public static void addCommand(Command command) { commands.add(command);}
}
