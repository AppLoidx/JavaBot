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
        return "16:52 09.03.2019";
    }
}
