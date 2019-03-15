package core.commands;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.VKCommands.VKCommand;
import core.common.KeysReader;
import core.modules.custom.commands.CommandList;
import core.modules.custom.commands.CustomCommandDB;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class TimedCommand extends Command implements VKCommand, Helpable {
    @Override
    public String getManual() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String exec(Message message) {
        String body = message.getBody();
        Map<String, String> keyMap = KeysReader.readKeys(body);
        String command;
        String time = null;
        CustomCommandDB db = new CustomCommandDB();
        try {
            CommandList cl = db.getList(message.getUserId());

            // Удаление записи

        if (keyMap.containsKey("-d")){
            if (cl == null){
                return "Ваш список команд пуст";
            }
            String id = keyMap.get("-d");
            if (id.matches("[0-9]*")){
                cl.deleteByID(id);
                db.addList(cl, message.getUserId());
                return "Ваша команда с ID " + id + " успешно удалена!";
            }
        }

        if (cl == null){
            cl = new CommandList();
        }

        // Создание записи
            if (keyMap.containsKey("-t")){
                time = keyMap.get("-t");
                if(!time.matches("[0-2][0-9]:[0-5][0-9]"))
                    return "Введите правильный формат времени ([0-2][0-9]:[0-5][0-9])";

            }

            if (keyMap.containsKey("-c")){
                command = keyMap.get("-c");
                if (time != null){
                    cl.addCommand(time, command);
                    db.addList(cl, message.getUserId());
                    return "Комадна \'"+command+"\' успешно добавлена на " + time;
                }
            }

            if (keyMap.containsKey("-s")){
                if (cl.getList().isEmpty()){
                    return "Ваш список команд пуст!";
                }
                StringBuilder sb = new StringBuilder();
                for(String timeKey : cl.getList().keySet()){
                    if (time != null && !timeKey.equals(time)) continue;
                    HashMap<String, String> cmdMap = cl.getList().get(timeKey);
                    for (String cmd : cmdMap.keySet()) {
                        sb.append("--------------------------\n");
                        sb.append("Время: ").append(timeKey).append("\n");
                        sb.append("Команда: ").append(cmd).append("\n");
                        sb.append("ID: ").append(cmdMap.get(cmd)).append("\n");
                        sb.append("---------------------------");
                    }
                }

                return sb.toString();
            } else {
                return "Воспользуйтесь справкой help timed-command";
            }
        } catch (SQLException e) {
            return "ошибка при работе с БД "+ e.getMessage();
        }
    }

    @Override
    protected void setConfig() {
        commandName = "timed-command";
    }
}
