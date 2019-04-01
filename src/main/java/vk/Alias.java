package vk;

import com.google.gson.GsonBuilder;
import com.vk.api.sdk.objects.messages.Message;
import core.modules.AliasDB;
import core.modules.vkSDK.MessageConverter;

import java.sql.SQLException;

/**
 * @author Arthur Kupriyanov
 */
public class Alias {
    private static AliasDB db = new AliasDB();

    public static String getAlias(int vkid, String alias){
        try {
            if (db.checkExist(vkid)){
                return db.getAlias(vkid).get(alias);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean isAlias(int vkid, String alias){
        try {
            if (db.checkExist(vkid) && db.getAlias(vkid).contains(alias)){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static Message setMessageBody(Message oldMessage, String newBody){
        return MessageConverter.getInstance(new GsonBuilder().create().toJson(oldMessage)).setBody(newBody).buildMessage();
    }
}
