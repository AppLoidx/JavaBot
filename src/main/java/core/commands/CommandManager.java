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
    static VKCore vkCore;

    {
        try {
            vkCore = new VKCore();
        } catch (IOException | ClientException | ApiException e) {
            e.printStackTrace();
        }
    }

    static {
        commands.add(new Schedule());
        commands.add(new Link());
        commands.add(new Unknown());
        commands.add(new Queue());
        commands.add(new Reg());
        commands.add(new Day());
        commands.add(new Command() {
            @Override
            public String init(String... args) {
                return "Привет";
            }

            @Override
            protected void setName() {
                name = "привет";
            }
        });

        commands.add(new Test(vkCore));
    }

    public static ArrayList<Command> getCommands(){
        return commands;
    }
}
