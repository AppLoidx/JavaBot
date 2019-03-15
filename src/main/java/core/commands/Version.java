package core.commands;

/**
 * @author Arthur Kupriyanov
 */
public class Version extends Command{
    @Override
    protected void setConfig() {
        commandName = "version";
    }

    @Override
    public String init(String... args) {
        return "23:40 15.03.2019";
    }
}
