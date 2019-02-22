package core.commands;

import core.common.KeysReader;
import core.common.UserInfoReader;
import core.modules.notice.NotificationNotFoundException;
import core.modules.notice.Notifications;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class Note extends Command {
    @Override
    public String init(String... args) {
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

        return "Не использовано ни одного ключа";
    }

    @Override
    protected void setName() {
        commandName = "note";
    }
}
