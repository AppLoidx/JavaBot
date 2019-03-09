package core.commands;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.VKCommands.VKCommand;
import core.common.KeysReader;
import core.common.UserInfoReader;
import core.modules.UsersDB;
import core.modules.notice.NotificationNotFoundException;
import core.modules.notice.Notifications;
import vk.VKManager;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Arthur Kupriyanov
 */
public class Note extends Command implements ProgramSpecification, VKCommand, Helpable {
    @Override
    public String init(String... args) {
        if (UserInfoReader.checkIsProgramm(args)){
            return programInit(args);
        }
        Map<String, String> keyMap = KeysReader.readKeys(args);
        Notifications notifications = new Notifications();

        // Добавление записи
        if (keyMap.containsKey("-a")){
            notifications.addNotification(keyMap.get("-a"), Integer.parseInt(UserInfoReader.readUserID(args)));
            return "Ваша запись успешно добавлена";
        }

        // Показать все объявления
        if (keyMap.containsKey("-s")){
            return notifications.getFormattedNotifications();
        }

        // Удаление объявления
        if (keyMap.containsKey("-d")){
            try {
                int ID = Integer.parseInt(keyMap.get("-d"));
                try {
                    if (notifications.getAuthorID(ID) == Integer.parseInt(UserInfoReader.readUserID(args))){
                        notifications.deleteNotification(ID);
                        return "Запись с ID " + ID + " удалена";
                    } else {
                        return "Объявление может удалить только его автор";
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return e.toString();
                } catch (NotificationNotFoundException e) {
                    return "Запись с таким номером не существует. Проверьте правильность введенных данных";
                } catch (NumberFormatException e){
                    return "Внутренняя ошибка при передаче ID пользователя";
                }
            } catch (NumberFormatException e) {
                return "Введите верный формат ключа -d";
            }
        }



        return "Не использовано ни одной дествующей функции";
    }

    public String programInit(String ... args){

        Map<String, String> keyMap = KeysReader.readKeys(args);
        Notifications notifications = new Notifications();

        if (keyMap.containsKey("-s")){
            return notifications.getJSONNotifications();
        }

        if (keyMap.containsKey("-a")){
            if (UserInfoReader.readUserID(args) == null){
                return "400";
            }
            try {
                UsersDB usersDB = new UsersDB();
                if (!usersDB.checkUserExist(Integer.parseInt(UserInfoReader.readUserID(args)))){
                    return "404";
                }
                notifications.addNotification(keyMap.get("-a"), Integer.parseInt(UserInfoReader.readUserID(args)));
                return "200";
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (keyMap.containsKey("-d")){
            try {
                int ID = Integer.valueOf(keyMap.get("-d").trim());
                try {
                    String userID = UserInfoReader.readUserID(args);

                    if (userID == null){
                        return "400";
                    }
                    if (notifications.getAuthorID(ID) == Integer.parseInt(userID)){
                        notifications.deleteNotification(ID);
                        return "200";
                    } else {
                        return "403";
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (NotificationNotFoundException e) {
                    return "404";
                }
            } catch (NumberFormatException e) {
                return "400";
            }
        }
        return "400";
    }
    @Override
    protected void setConfig() {
        commandName = "note";
    }

    @Override
    public String exec(Message message) {
        if (message.getFwdMessages() != null){
            Message fwdMessage = message.getFwdMessages().get(0);
            String parsedMessageData = fwdMessage.getBody().split("\n")[0];
            if (!parsedMessageData.equals("error") && !parsedMessageData.equals("empty")){
                String noteID = parsedMessageData.split(" ")[1];

                Notifications notifications = new Notifications();
                try {
                    int authorID = notifications.getAuthorID(Integer.valueOf(noteID));
                    if (authorID == message.getUserId()){
                        notifications.deleteNotification(Integer.valueOf(noteID));
                        return "Запись с номером " + noteID + " успешно удалена";
                    } else {
                        return "У вас нет прав на удаление этой записи";
                    }
                } catch (SQLException | NotificationNotFoundException e) {
                    e.printStackTrace();
                    return "Внутренняя ошибка " + e.toString();
                }
            } else {
                return "Данное объявление невозможно удалить";
            }
        }

        String[] response = message.getBody().split(" ");
        Map<String, String> keyMap = KeysReader.readKeys(response);
        if (keyMap.containsKey("-s")){
            Notifications notifications = new Notifications();
            Stream<String> noteStream = notifications.getNotificationsStream();
            VKManager VKManager = new VKManager();
            int peerID = message.getUserId();
            noteStream.forEach(str -> VKManager.sendMessage(str,peerID));
            return "";
        }

        return null;
    }

    @Override
    public String getManual() {
        return
                "Ключи:\n" +
                        "\t-a [заметка] = добавить заметку\n" +
                        "\t-d [номер_заметки] = удалить заметку\n" +
                        "\t-s  = показать все заметки\n" +
                        "Также монжо использовать следующий метод:\n" +
                        "Выбрать заметку, ответить не неё с сообщением note, " +
                        "тогда она сразу удалиться без ввода ID.";
    }

    @Override
    public String getDescription() {
        return "Команда для объявлений";
    }
}
