package core.commands;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.VKCommands.VKCommand;
import core.modules.AliasDB;
import core.modules.Aliases;

import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Arthur Kupriyanov
 */
public class Alias extends Command implements VKCommand, Helpable, TelegramCommand {
    @Override
    protected void setConfig() {
        commandName = "alias";
    }

    @Override
    public String exec(Message message) {
        try {
            AliasDB db = new AliasDB();
            String[] splitedText = message.getBody().split(" ");
            if (splitedText.length > 1){
                switch (splitedText[1]){
                    case "show":
                        if (!db.checkExist(message.getUserId())) return "Вы еще не добавили ни одного alias";
                        StringBuilder sb = new StringBuilder();
                        Map<String, String> aliasMap = db.getAlias(message.getUserId()).getAll();
                        for(String key : aliasMap.keySet()){
                            sb.append(key).append(" : ").append(aliasMap.get(key)).append("\n");
                        }
                        return sb.toString();
                    case "delete":
                        if (splitedText.length < 3) return "Укажите alias, который хотите удалить";
                        if (!db.checkExist(message.getUserId())) return "Вы еще не добавили ни одного alias";
                        Aliases aliases = db.getAlias(message.getUserId());
                        StringBuilder response = new StringBuilder();
                        for (int i = 2; i < splitedText.length; i++){
                            if (aliases.contains(splitedText[i])){
                                aliases.delete(splitedText[i]);
                                response.append(splitedText[i]).append(" удален\n");
                            } else response.append(splitedText[i]).append(" не найден\n");
                        }
                        db.addAlias(message.getUserId(), aliases);
                        return response.toString();
                }
            }
            if (splitedText.length < 2){
                return "Вы ввели неверный формат. Воспользуйтесь help";
            }
            String command;

            String alias;

            Pattern p = Pattern.compile(" [a-zA-Z0-9]*=\".+\".*");
            Matcher matcher = p.matcher(message.getBody());

            if (matcher.find()){
                String[] splitedGroup = matcher.group().split("=");
                command = splitedGroup[1].substring(1, splitedGroup[1].length() - 1);
                alias = splitedGroup[0];
            }
            else return "Неверный формат. Воспользуйтесь help";


            Aliases userAliases;

                if (db.checkExist(message.getUserId())){
                    userAliases = db.getAlias(message.getUserId());
                } else {
                    userAliases = new Aliases();
                }
                userAliases.add(alias, command);
                db.addAlias(message.getUserId(), userAliases);
                return "Добавлен alias: " + alias + " как команда: " + command;

        } catch (SQLException e) {
            e.printStackTrace();
            return "Ошибка при работе с БД. " + e.getMessage();
        }
    }

    @Override
    public String getManual() {
        return "Чтобы создать alias, введите:\n" +
                "alias alias_name=\"command_of_alias\"\n" +
                "\nНапример,\n" +
                "alias now=\"schedule\"\n\n" +
                "Посмотреть список созданных alias:\n" +
                "alias show\n\n" +
                "Удаление alias:\n" +
                "alias delete name_of_alias\n\n" +
                "Подробнее:\n" +
                "https://github.com/AppLoidx/JavaBot/wiki/команда-Alias";
    }

    @Override
    public String getDescription() {
        return "Создание alias";
    }

    @Override
    public String telegramExec(Message message) {
        return exec(message);
    }
}
