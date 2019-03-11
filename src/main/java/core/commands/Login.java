package core.commands;

import core.common.KeysReader;

import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class Login extends Command implements ProgramSpecification{

    @Override
    public String init(String ... args) {
        return null;
    }

    @Override
    protected void setConfig() {
        commandName = "log";
    }

    @Override
    public String programInit(String... args) {
        Map<String, String> keyMap = KeysReader.readKeys(args);

        return null;
    }
}
