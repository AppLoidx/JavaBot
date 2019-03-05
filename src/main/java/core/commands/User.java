package core.commands;

import core.common.KeysReader;
import core.modules.UsersDB;
import org.json.simple.JSONObject;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class User extends Command{

    @Override
    public String init(String ... args) {
        Map<String, String > keyMap = KeysReader.readKeys(args);
        JSONObject jsonObject = new JSONObject();

        if (keyMap.containsKey("-l") && keyMap.containsKey("-p")){
            try {
                UsersDB usersDB = new UsersDB();
                int authStatus = usersDB.auth(keyMap.get("-l"), keyMap.get("-p"));
                jsonObject.put("status", "200");
                jsonObject.put("response_status", String.valueOf(authStatus));
                if (authStatus > 0) {
                    jsonObject.put("fullname", usersDB.getFullNameByVKID(authStatus));
                }
                usersDB.closeConnection();
                return jsonObject.toString();
            } catch (SQLException e) {
                jsonObject.put("status", "500");
                return jsonObject.toString();
            }
        } else if (keyMap.containsKey("-l") || keyMap.containsKey("-p")){
            jsonObject.put("status", "400");
            return jsonObject.toString();
        }

        jsonObject.put("status", "400");
        return jsonObject.toString();
    }

    @Override
    protected void setConfig() {
        commandName = "user";
    }
}
