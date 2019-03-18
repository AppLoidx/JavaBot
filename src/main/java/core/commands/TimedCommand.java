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
 * Команда для настройки команд по расписанию
 * @author Arthur Kupriyanov
 */
public class TimedCommand extends Command implements VKCommand, Helpable {
    @Override
    public String getManual() {
        return
                "Данная команда предназначена для выполнения команд от вашего имени " +
                        "в заданное время. Например, если добавите команду schedule на 08:00, " +
                        "то в это время выполнится эта команда и вы получите сообщение от бота.\n" +
                        "Чтобы добавить команду, необходимо указать время и саму команду:\n\n" +
                        "-t время -- задать время в формате HH:mm\n" +
                        "-c % команда % -- задать команду внутри спецсимволов %\n\n" +
                        "Дополнительные ключи для редактирования:\n" +
                        "-s -- показать все добавленные команды\n" +
                        "-d номер_команды -- удалить команду\n" +
                        "\n" +
                        "Команды необходимо вводить внутри знаках процентов, так как происходит " +
                        "конфликт между ключами самой команды timed-command и его значения в " +
                        "ключе -с. Все что будет введно внутри спецсимволов - не будет считаться " +
                        "как часть команды, в том числе и его ключи.\n" +
                        "В данный момент есть реалзиация лишь команд, которые будут выполняться " +
                        "ежедневно.\n" +
                        "Команда исполнится не сразу, а через максимум 20 сек после того как наступит " +
                        "время. Это связано с тем, чтобы оптимизровать обращения к базе данных и потребление " +
                        "ресурсов сервера.";
    }

    @Override
    public String getDescription() {
        return "Команда по расписанию (пока только ежедневная)";
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
            StringBuilder msg = new StringBuilder();
            for (String id : keyMap.get("-d").split(" "))
            if (id.matches("[0-9]*")){
                cl.deleteByID(id);
                db.addList(cl, message.getUserId());
                msg.append("Ваша команда с ID ").append(id).append(" успешно удалена!\n");
            }

            return msg.toString();
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
