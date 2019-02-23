package core.common;

import java.util.Map;

/**
 * @version 1.1
 * @author Arthur Kupriyanov
 */
public class UserInfoReader {
    private final static String META_DATA_IDENTIFICATION = "--#";

    public static String readUserID(String ... args){
        Map<String, String> keysMap = KeysReader.readKeys(args);

        String metaDataKey = META_DATA_IDENTIFICATION + "user_id";

        if (keysMap.containsKey(metaDataKey)){
            return keysMap.get(metaDataKey);
        }

        return null;
    }
    public static String readUserFirstName(String ... args){
        Map<String, String> keysMap = KeysReader.readKeys(args);

        String metaDataKey = META_DATA_IDENTIFICATION + "first_name";

        if (keysMap.containsKey(metaDataKey)){
            return keysMap.get(metaDataKey);
        }

        return null;
    }
    public static String readUserLastName(String ... args){
        Map<String, String> keysMap = KeysReader.readKeys(args);

        String metaDataKey = META_DATA_IDENTIFICATION + "last_name";

        if (keysMap.containsKey(metaDataKey)){
            return keysMap.get(metaDataKey);
        }

        return null;
    }

    public static boolean checkIsProgramm(String ... args){
        Map<String, String> keysMap = KeysReader.readKeys(args);

        String metaDataKey = META_DATA_IDENTIFICATION + "program";

        return keysMap.containsKey(metaDataKey);

    }


}
