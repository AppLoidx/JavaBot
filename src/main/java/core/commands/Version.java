package core.commands;

import core.common.KeysReader;
import core.modules.Date;

import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class Version extends Command implements Helpable{
    private static String date = Date.getDate();
    @Override
    protected void setConfig() {
        commandName = "version";
    }

    @Override
    public String init(String... args) {
        Map<String, String> keyMap = KeysReader.readKeys(args);

        if (keyMap.containsKey("-i")){
            String ver = "";
            ver += "Добавлена команда timed-command\n";
            ver += "Добавлена команда news\n";
            ver += "Добавлены документации к некоторым командам";

            return ver;
        }
        return date;
    }

    @Override
    public String getManual() {
        return "По умолчанию возвращает дату деплоя\n" +
                "С ключом -i возвращает краткое описание изменений";
    }

    @Override
    public String getDescription() {
        return "Информация о версии";
    }
}
