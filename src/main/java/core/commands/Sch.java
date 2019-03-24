package core.commands;

import core.Commander;

/**
 * @author Arthur Kupriyanov
 */
public class Sch extends Command {
    @Override
    protected void setConfig() {
        commandName = "sch";
    }

    @Override
    public String init(String... args) {
        return new Schedule().init(args);
    }
}
