package core.commands;


import core.common.JSONReader;
import core.common.KeysReader;
import core.modules.LinksDB.LinksDB;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Артур Куприянов
 * @version 2.0
 */
public class Link extends Command {

    private LinksDB linksDB;

    @Override
    protected void setName() {
        name = "link";
    }

    Link(){
             linksDB = new LinksDB();
    }

    /**
     * Возвращает ссылки по ключу
     */
    @Override
    public String init(String... args) {

        StringBuilder response = new StringBuilder();
        TreeMap<Integer, Map<String, String>> keysMap = KeysReader.readOrderedKeys(args);
        for (int key: keysMap.keySet()
             ) {
            try {
                String DBKey = String.valueOf(keysMap.get(key).keySet().toArray()[0]);
                String link = linksDB.getLinkByKey(DBKey);
                if (link!=null){
                    response.append(linksDB.getNameByKey(DBKey));
                    response.append("\n");
                    response.append(link);
                    response.append("\n");
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (response.length() == 0){
            return "Укажите параметры [-iop]";
        }else{
            return response.toString();
        }

    }
}
