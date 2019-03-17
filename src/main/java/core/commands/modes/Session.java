package core.commands.modes;

import com.vk.api.sdk.objects.messages.Message;
import core.CommandManager;
import core.commands.Command;
import core.commands.Helpable;
import core.commands.Mode;
import core.commands.VKCommands.VKCommand;
import core.modules.session.SessionManager;
import core.modules.session.UserIOStream;

import java.util.List;
import java.util.Set;

/**
 * @author Arthur Kupriyanov
 */
public class Session extends Command implements VKCommand, Helpable {
    @Override
    protected void setConfig() {
        commandName = "session";
    }

    @Override
    public String exec(Message message) {
        String[] inputArray = message.getBody().split(" ");
        if (inputArray.length < 2){
            return "Неверный формат команды. Воспользуйтесь help session";
        }
        if (inputArray[1].equals("exit")){
            if (SessionManager.checkExist(message.getUserId())){
                SessionManager.deleteSession(message.getUserId());
            } else {
                return "Вы уже вышли или не заходили в какой-либо режим";
            }
        }
        if (inputArray[1].equals("help")){
            if (inputArray.length < 3){
                Set<Mode> modes = SessionManager.getModeList();
                StringBuilder description = new StringBuilder();
                for (Mode mode : modes){
                    if (mode instanceof Helpable){
                        description.append(mode.getName()).append(" -- ").append(((Helpable) mode).getDescription()).append("\n");
                    } else {
                        description.append(mode.getName()).append(" -- нет описания");
                    }
                }
                return "Список доступных сессий:\n" + description.toString();
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i=2; i<inputArray.length; i++){
                    for(Mode mode:SessionManager.getModeList()){
                        if (mode.getName().equals(inputArray[i])){
                            sb.append("\n-------------------------\n");
                            if (mode instanceof Helpable){
                                sb.append(mode.getName());
                                sb.append("\n").append(((Helpable) mode).getManual());
                            } else {
                                sb.append("Данная сессия не поодерживает документацию");
                            }
                        }
                    }
                }
                return sb.toString();
            }
        }
        for (Mode mode : SessionManager.getModeList()){
                if (mode.getName().equals(inputArray[1])){
                    UserIOStream input = new UserIOStream();
                    UserIOStream output = new UserIOStream();
                    core.modules.session.Session session = new core.modules.session.Session(message.getUserId(), mode, input, output);
                    SessionManager.addSession(message.getUserId(), session);
                    new Thread(session.getMode()).start();
                    return "Вы переключились на режим " + mode.getName();
                }
        }
        return "Запращиваемый мод не найден";
    }

    @Override
    public String getManual() {
        return "Команда для начала новой сессии с определенным режимом. Это нужно для перенаправления " +
                "ввода пользователя в определенные команды. Учтите, что в данный момент команды, " +
                "которые есть у бота и, те которые находятся внутри мода - разные. Иными словами, " +
                "находясь внутри какого-либо мода вы не сможете воспользоваться командой бота.\n\n" +
                "Команда выхода предусмотрена в самом режиме.\n\n" +
                "Сессия работает в двух режимах:\n" +
                "-Вывода мануала\n" +
                "-Запуск сессии\n" +
                "\n" +
                "Чтобы получить список доступных сессий введите:\n" +
                "session help\n" +
                "Чтобы получить расширенный мануал по режиму:\n" +
                "session help имя_сессии1 имя_сессии2\n" +
                "\n" +
                "Чтобы перейти к работе с сессией введите команду:\n" +
                "session имя_сессии\n\n" +
                "Данная команда еще разрабатывается и возможно изменения в синтаксисе. Следите за мануалом.";
    }

    @Override
    public String getDescription() {
        return "Начать сессию";
    }
}
