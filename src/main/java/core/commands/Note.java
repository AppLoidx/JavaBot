package core.commands;

import core.common.KeysReader;
import core.common.UserInfoReader;
import core.modules.Notifications;

import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class Note extends Command {
    @Override
    public String init(String... args) {
        Map<String, String> keyMap = KeysReader.readKeys(args);
        Notifications notifications = new Notifications();

        if (keyMap.containsKey("-a")){
            notifications.addNotification(keyMap.get("-a"), Integer.valueOf(UserInfoReader.readUserID(args)));
            return "Ваша запись успешно добавлена";
        }

        if (keyMap.containsKey("-s")){
            return notifications.getFormattedNotifications();
        }

        return "Не использовано ни одного ключа";
    }

    @Override
    protected void setName() {
        commandName = "note";
    }
}
