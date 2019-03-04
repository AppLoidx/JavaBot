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
            String group;

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
