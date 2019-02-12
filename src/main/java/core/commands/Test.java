package core.commands;

import core.common.KeysReader;

import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class Test  extends Command{

    @Override
    public String init(String... args) {

        Map<String, String> keysMap = KeysReader.readKeys(args);

        System.out.println(keysMap.get("--#user_id"));
        System.out.println(keysMap.get("--#first_name"));
        System.out.println(keysMap.get("--#last_name"));

        return null;
    }

    @Override
    protected void setName() {
        this.name = "test";
    }
}
