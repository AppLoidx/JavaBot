package core.commands;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import core.commands.special.vk.Test;
import vk.VKCore;

import java.io.IOException;
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
        commands.add(new Reg());
        commands.add(new Day());
        commands.add(new Note());
    }

    public static ArrayList<Command> getCommands(){
        return commands;
    }
}
