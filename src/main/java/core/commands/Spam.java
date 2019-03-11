package core.commands;

import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import core.commands.VKCommands.VKCommand;
import core.common.KeysReader;
import core.modules.UsersDB;
import vk.VKManager;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class Spam extends Command implements VKCommand {
    @Override
    protected void setConfig() {
        commandName = "spam";
    }

    @Override
    public String exec(Message message) {
        Map<String, String> keyMap = KeysReader.readKeys(message.getBody().split(" "));
        String group;
        if (keyMap.containsKey("-g")){
            group = keyMap.get("-g");
        } else {
            return "укажите группу через ключ -g";
        }

        String msg;
        if (keyMap.containsKey("-m")){
            msg = keyMap.get("-m");
        } else {
            return "Укажите сообщение через ключ -m";
        }

        UserXtrCounters user = new VKManager().getUserInfo(message.getUserId());
        msg += "\nСообщение от: " + user.getFirstName() + " " + user.getLastName();

        UsersDB usersDB = new UsersDB();
        try {
            Map<Integer, String> users = usersDB.getVKIDListWithGroup();
            for (int vkid : users.keySet()){
                if (users.get(vkid).equals(group)){
                    sendMessage(vkid, msg);
                }
            }
            return "Хорошо!";

        } catch (SQLException e) {
            return "Ошибка " + e.getMessage();
        }

    }

    private void sendMessage(int vkid, String msg){
        new VKManager().sendMessage(msg, vkid);
    }
}
