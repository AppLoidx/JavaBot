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
        commandName = "ver";
    }

    @Override
    public String init(String... args) {
        Map<String, String> keyMap = KeysReader.readKeys(args);

        if (keyMap.containsKey("-i")){
            String ver = "";
            ver += "Добавлена команда tess\n";
            ver += "Добавлена обработка натурального языка\n";
            ver += "Добавлена команда session\n";
            ver += "Добавлена режим tracer - эмулятор БЭВМ\n";
            ver += "Добавлена команда config";

            return ver;
        }
        return date;
    }

    @Override
    public String getManual() {
        return "По умолчанию возвращает дату деплоя\n" +
                "С ключом -i возвращает краткое описание последних изменений";
    }

    @Override
    public String getDescription() {
        return "Информация о версии";
    }
}
