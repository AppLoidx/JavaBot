package core.common;

import java.util.Map;

/**
 * @version 1.0
 * @author Arthur Kupriyanov
 */
public class UserInfoReader {
    public static String readUserID(String ... args){
        Map<String, String> keysMap = KeysReader.readKeys(args);

        if (keysMap.containsKey("--#user_id")){
            return keysMap.get("--#user_id");
        }

        return null;
    }
    public static String readUserFirstName(String ... args){
        Map<String, String> keysMap = KeysReader.readKeys(args);

        if (keysMap.containsKey("--#first_name")){
            return keysMap.get("--#first_name");
        }

        return null;
    }
    public static String readUserLastName(String ... args){
        Map<String, String> keysMap = KeysReader.readKeys(args);

        if (keysMap.containsKey("--#last_name")){
            return keysMap.get("--#last_name");
        }

        return null;
    }


}
