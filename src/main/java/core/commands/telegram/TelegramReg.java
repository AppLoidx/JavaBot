package core.commands.telegram;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.TelegramCommand;
import core.commands.VKCommands.VKCommand;
import core.common.KeysReader;
import core.common.LocaleMath;
import core.modules.telegram.TelegramAuthDB;
import core.modules.telegram.TelegramUsersDB;
import vk.VKManager;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class TelegramReg extends TelegramCommand implements VKCommand {
    @Override
    protected void setConfig() {
        commandName = "reg";
    }

    @Override
    public String init(String... args) {
        return super.init(args);
    }

    @Override
    public String exec(Message message) {

        Map<String, String> keysMap = KeysReader.readKeys(message.getBody());

        if (keysMap.containsKey("-v")){
            if (keysMap.get("-v").matches("[0-9]+")){
                int vkid = Integer.parseInt(keysMap.get("-v"));
                try {
                    int generatedPassword = LocaleMath.randInt(100000, 999999);
                    new TelegramAuthDB().newReg(message.getUserId(), vkid, generatedPassword);
                    new VKManager().sendMessage("Вот ваш пароль для подключения аккаунта Telegram\n:" +
                            "" + generatedPassword, vkid);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if (message.getBody().matches("reg [0-9]+")){
            int password = Integer.parseInt(message.getBody().split(" ")[1]);
            TelegramAuthDB db = new TelegramAuthDB();

            try {
                int generatedPwd = db.getPassword(message.getUserId());

            if (password == generatedPwd){
                new TelegramUsersDB().add(message.getUserId(), db.getVKID(message.getUserId()));
                return "Вы добавлены в базу данных";
            }else {
                return "Пароль не совпадает. Повторите попытку снова с новой командой reg";
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
