package core.commands;

import com.vk.api.sdk.objects.users.User;
import core.common.KeysReader;
import core.common.UserInfoReader;
import core.modules.notice.NotificationNotFoundException;
import core.modules.notice.Notifications;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class Note extends Command implements ProgramSpecification{
    @Override
    public String init(String... args) {
        if (UserInfoReader.checkIsProgramm(args)){
            return programInit(args);
        }
        Map<String, String> keyMap = KeysReader.readKeys(args);
        Notifications notifications = new Notifications();

        // Добавление записи
        if (keyMap.containsKey("-a")){
            notifications.addNotification(keyMap.get("-a"), Integer.valueOf(UserInfoReader.readUserID(args)));
            return "Ваша запись успешно добавлена";
        }

        // Показать все объявления
        if (keyMap.containsKey("-s")){
            return notifications.getFormattedNotifications();
        }

        // Удаление объявления
        if (keyMap.containsKey("-d")){
            try {
                int ID = Integer.valueOf(keyMap.get("-d"));
                try {
                    if (notifications.getAuthorID(ID) == Integer.valueOf(UserInfoReader.readUserID(args))){
                        notifications.deleteNotification(ID);
                        return "Запись с ID " + ID + " удалена";
                    } else {
                        return "Объявление может удалить только его автор";
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (NotificationNotFoundException e) {
                    return "Запись с таким номером не существует. Проверьте правильность введенных данных";
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
            notifications.addNotification(keyMap.get("-a"), Integer.valueOf(UserInfoReader.readUserID(args)));
            return "200";
        }

        if (keyMap.containsKey("-d")){
            try {
                int ID = Integer.valueOf(keyMap.get("-d").trim());
                try {
                    String userID = UserInfoReader.readUserID(args);

                    if (userID == null){
                        return "400";
                    }
                    if (notifications.getAuthorID(ID) == Integer.valueOf(userID)){
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
    protected void setName() {
        commandName = "note";
    }
}
