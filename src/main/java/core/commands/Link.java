package core.commands;


import core.common.JSONReader;
import core.common.KeysReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Артур Куприянов
 * @version 1.2
 */
public class Link extends Command {
    private JSONObject jsonObjectData;

    @Override
    protected void setName() {
        name = "link";
    }

    Link(){
        try {
            jsonObjectData = JSONReader.getJSONObject("src/main/botResources/links.json");
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ключи: <br>
     *  -o | --opd - журнал по ОПД <br>
     *  -p | --programming - журнал по программирвоанию <br>
     *  -i | --informatics - журнал по информатике <br>
     */
    @Override
    public String init(String... args) {

        StringBuilder response = new StringBuilder();
        TreeMap<Integer, Map<String, String>> keysMap = KeysReader.readOrderedKeys(args);
        for (int key: keysMap.keySet()
             ) {
            if (keysMap.get(key).containsKey("--opd") || keysMap.get(key).containsKey("-o")) {
                response.append("ОПД : ");
                response.append(jsonObjectData.get("opd").toString());
                response.append("\n");
            }
            if (keysMap.get(key).containsKey("--informatics") || keysMap.get(key).containsKey("-i")) {
                response.append("Информатика : ");
                response.append(jsonObjectData.get("informatics").toString());
                response.append("\n");
            }
            if (keysMap.get(key).containsKey("--programming") || keysMap.get(key).containsKey("-p")) {
                response.append("Программирование : ");
                response.append(jsonObjectData.get("programming").toString());
                response.append("\n");
            }
        }

        if (response.length() == 0){
            return "Укажите параметры [-iop]";
        }else{
            return response.toString();
        }

    }
}
