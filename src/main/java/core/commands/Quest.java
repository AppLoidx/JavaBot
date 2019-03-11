package core.commands;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.VKCommands.VKCommand;
import core.modules.QuestionsDB;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Arthur Kupriyanov
 */
public class Quest extends Command implements VKCommand, Helpable {
    @Override
    protected void setConfig() {
        commandName = "quest";
    }

    @Override
    public String exec(Message message) {
        String[] body = message.getBody().split(" ");
        try {
            ArrayList<String> q = new QuestionsDB().getQuestions();
            if (body.length > 1) {
                try {
                    int count = Integer.parseInt(body[1]);
                    if (q.size() < count){
                        count = q.size();
                    } else {count--;}

                if (body.length > 2){
                    try {
                        int startId = Integer.parseInt(body[2]);
                        String msg = "";
                        int key = 0;
                        int keyCount = 0;
                        for (String s : q){
                            if (keyCount <= count && key>= startId){
                            msg += key + ". " + s + "\n\n";
                            keyCount++;}
                            key++;
                        }
                        if (msg.equals("")){
                            return "пусто";
                        }
                        return msg;
                    } catch (NumberFormatException e){
                        return "Введен неверный формат для ID вопроса";
                    }
                } else {
                    StringBuilder msg = new StringBuilder();
                    int key = 0;
                    for (String s : q){
                        if (key++ <= count) {
                            msg.append(key).append(". ");
                            msg.append(s).append("\n\n");
                        }
                    }
                    if (msg.toString().equals("")){
                        return "пусто";
                    }

                    return msg.toString();
                }

                } catch (NumberFormatException e) {
                    return "Введите верный формат количества вопросов";
                }
            } else {
                return "Введите параметры или воспользуйтесь документацией help";
            }


        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    @Override
    public String getManual() {
        return "Чтобы получить вопросы, выполните команду:\n" +
                "quest [кол_вопросов] [id_первого вопроса]\n" +
                "\nНа данный момент реализованы лишь вопросы по Java Collections и выборка из них тоже сильно ограничена." +
                "\nКоманда выводит последовательное количество вопросов. Чтобы указать количество выводимых вопросов, " +
                "укажите их первым аргументом. Так как вопросы последовательные, то можно указать id первого вопроса, " +
                "откуда выберутся последвовательно следующие n вопросов.\n" +
                "Альфа версия команды.";
    }

    @Override
    public String getDescription() {
        return "Получить вопросы по Java (пока только Collections)";
    }
}
