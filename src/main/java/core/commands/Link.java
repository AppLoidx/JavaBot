package core.commands;


import core.common.JSONReader;
import core.common.KeysReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Map;

public class Link extends Command {
    private JSONObject jsonObjectData;
    Link(){
        try {
            jsonObjectData = JSONReader.getJSONObject("src/main/botResources/links.json");
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String init(String... args) {

        Map<String, String> keysMap = KeysReader.readKeys(args);
        StringBuilder response = new StringBuilder();

        if(keysMap.containsKey("--opd") || keysMap.containsKey("-o")){
            response.append("ОПД : ");
            response.append(jsonObjectData.get("opd").toString());
            response.append("\n");
        }
        if(keysMap.containsKey("--informatics") || keysMap.containsKey("-i")){
            response.append("Информатика : ");
            response.append(jsonObjectData.get("informatics").toString());
            response.append("\n");
        }
        if(keysMap.containsKey("--programming") || keysMap.containsKey("-p")){
            response.append("Программирование : ");
            response.append(jsonObjectData.get("programming").toString());
            response.append("\n");
        }

        if (response.length() == 0){
            return "Укажите параметры [-iop]";
        }else{
            return response.toString();
        }

    }
}
