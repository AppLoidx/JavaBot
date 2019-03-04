package core.commands;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import core.commands.VKCommands.VKCommand;
import core.common.KeysReader;
import core.common.UserInfoReader;
import core.modules.UsersDB;
import vk.MessageSender;

import java.sql.SQLException;
import java.util.Map;

/**
 * @version 1.0
 * @author Arthur Kupriyanov
 */
public class Reg extends Command implements VKCommand {
    public static void main(String[] args) {
        try {
            UsersDB usersDB = new UsersDB();
            System.out.println(usersDB.getFullNameByVKID(255396611));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String init(String... args) {
        Map<String, String> keysMap = KeysReader.readKeys(args);

        int vkid = Integer.parseInt(UserInfoReader.readUserID(args));
        String name = UserInfoReader.readUserFirstName(args);
        String lastname = UserInfoReader.readUserLastName(args);
        String login = null;
        String group;

        if (keysMap.containsKey("-g")){
            group = keysMap.get("-g");
        } else return "Укажите вашу группу с ключом -g";
        if (keysMap.containsKey("-l")) {
            login = keysMap.get("-l");
        }

        try {
            UsersDB usersDB = new UsersDB();
            if (usersDB.checkUserExsist(vkid)){
                    if (login != null){
                        usersDB.updateUserLogin(login, vkid);
                        return "Ваши данные были обновлены";
                    }
                return "Вы уже зарегестрированы под именем " + usersDB.getFullNameByVKID(vkid);
            }
            usersDB.addUser(name, lastname,vkid,group);
            if (login != null){
                usersDB.updateUserLogin(login, vkid);
            }
            usersDB.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Ошибка при работе с базой данных: " + e.toString();
        }

        return "Вы успешно добавлены в базу данных";

    }

    @Override
    protected void setConfig() {
        this.commandName = "reg";
    }

    @Override
    public String exec(Message message) {
        MessageSender ms = new MessageSender();
        UserXtrCounters info = ms.getUserInfo(message.getUserId());

        try {
            UsersDB usersDB = new UsersDB();
            int vkid = info.getId();
            String lastname = info.getLastName();
            String name = info.getFirstName();
            String group = null;

            Map<String, String> keyMap = KeysReader.readKeys(message.getBody().split(" "));


            if (!usersDB.checkUserExsist(vkid)) {
                if (keyMap.containsKey("-g")){
                    group = keyMap.get("-g");
                } else {
                    ms.sendMessage("Введите номер вашей группы через ключ -g.\n" +
                            "Например, reg -g P3112", message.getUserId());
                    return "";
                }
                usersDB.addUser(name, lastname, vkid, group);

                try {
                    ms.getSendQuery()
                            .message("Вы успешно зарегистрированы!")
                            .peerId(message.getUserId())
                            .attachment("photo-172998024_456239022")
                            .execute();
                } catch (ApiException | ClientException e) {
                    e.printStackTrace();
                }

                usersDB.closeConnection();
            } else {
                return "Вы уже зарегестрированы";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Ошибка при работе с базой данных: " + e.toString();
        }


        return "";
    }
}
