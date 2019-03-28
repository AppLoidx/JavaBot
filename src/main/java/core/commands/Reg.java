package core.commands;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import core.commands.VKCommands.VKCommand;
import core.common.KeysReader;
import core.modules.UsersDB;
import vk.VKManager;

import java.sql.SQLException;
import java.util.Map;

/**
 * @version 1.0
 * @author Arthur Kupriyanov
 */
public class Reg extends Command implements VKCommand , Helpable{
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
        VKManager ms = new VKManager();
        UserXtrCounters info = VKManager.getUserInfo(message.getUserId());

        try {
            UsersDB usersDB = new UsersDB();
            int vkid = info.getId();
            String lastname = info.getLastName();
            String name = info.getFirstName();
            String group;

            Map<String, String> keyMap = KeysReader.readKeys(message.getBody());


            if (!usersDB.checkUserExist(vkid)) {
                if (keyMap.containsKey("-g")){
                    group = keyMap.get("-g");
                    if (!group.matches("[a-zA-Z][0-9].*")){
                        return "Введите верный формат группы. Например, P3112";
                    }
                    if (group.equals("")){
                        ms.sendMessage("Вы не ввели номер группы", message.getUserId());
                    }
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

            } else {
                if (keyMap.containsKey("-l")){
                    String login = keyMap.get("-l");
                    if (!login.equals("")) {
                        if (usersDB.checkUserExist(login)){
                            return "Такой логин уже есть. Выберите другой";
                        }
                        usersDB.updateUserLogin(login, message.getUserId());
                        ms.sendMessage("Вы успешно измели свой логин на " + login, message.getUserId());
                    } else {
                        ms.sendMessage("Введите логин вместе с ключом -l", message.getUserId());
                    }
                } else return "Вы уже зарегестрированы";
                return "";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Ошибка при работе с базой данных: " + e.toString();
        }
        return "";
    }

    @Override
    public String getManual() {
        return "Ключи:\n" +
                "\t-g [номер_группы]\n" +
                "\t-l [логин]\n" +
                "Номер группы нужен, чтобы корректно отображать раписание, " +
                "доступ к очередям группы, групповых рассылок\n" +
                "Логин нужен для авторизации через сторонние графические приложения. " +
                "Пароль для этого не нужен.";
    }

    @Override
    public String getDescription() {
        return "Команда для регистрации";
    }
}
