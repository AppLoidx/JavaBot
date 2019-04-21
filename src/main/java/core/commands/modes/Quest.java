package core.commands.modes;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.Mode;
import core.modules.modes.quest.QuestMode;
import core.modules.modes.quest.Questions;
import core.modules.modes.quest.QuestionsDB;
import core.modules.modes.quest.SavedQuestions;
import core.modules.session.SessionManager;
import core.modules.session.UserIOStream;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Arthur Kupriyanov
 */
public class Quest implements Mode{
    private static final QuestionsDB db =  new QuestionsDB();
    private UserIOStream input;
    private UserIOStream output;
    private Questions questions;

    @Override
    public void setUserID(int id) {

    }

    @Override
    public String getName() {
        return "quest";
    }

    @Override
    public String getResponse(String input) {
        return null;
    }

    @Override
    public String getResponse(Message message) {
        String out;
        if (message.getBody().equals(":save")){
            try {

                SavedQuestions sq = db.getSavedQuestions(message.getUserId());
                if (sq == null) sq = new SavedQuestions();
                if (questions!=null){
                    sq.saveQuest(questions.getMode().getValue(), questions.getLastId());
                    db.saveQuestions(sq, message.getUserId());
                    return "Вы сохранили вопрос номер " + questions.getLastId() + " " +
                            "\nТема: " + questions.getMode().getValue();
                }
                else return "режим еще не инициализирован";

            } catch (SQLException e) {
                e.printStackTrace();
                return "Ошибка при сохранении [500]";
            }
        }
        if (message.getBody().matches(":save [a-zA-Z0-9]*")){
            try {

                SavedQuestions sq = db.getSavedQuestions(message.getUserId());
                if (sq == null) sq = new SavedQuestions();
                if (questions!=null){
                    sq.saveQuest(message.getBody().split(" ")[1], questions.getLastId());
                    db.saveQuestions(sq, message.getUserId());
                    return "Вы сохранили вопрос номер " + questions.getLastId() + " " +
                            "\nПользовательская тема: " + message.getBody().split(" ")[1];
                }
                else return "режим еще не инициализирован";

            } catch (SQLException e) {
                e.printStackTrace();
                return "Ошибка при сохранении [500]";
            }
        }
        if (message.getBody().matches(":my.*")){
            try {
                SavedQuestions sq = db.getSavedQuestions(message.getUserId());

                if (sq==null) return "Вы еще не сохранили вопросов";
                if (message.getBody().matches(":my [a-zA-Z0-9]*")){
                    StringBuilder sb = new StringBuilder();
                    for(String tag: message.getBody().split(" ")){
                        if (tag.equals(":my")) continue;
                        sb.append(formatSavedQuests(sq, tag));
                    }
                    return sb.toString();
                }

                return formatSavedQuests(sq);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (message.getBody().matches(":delete [a-zA-Z0-9]*.*")){

                if (message.getBody().matches(":delete .*[a-zA-Z0-9]*[.][0-9]*.*")) {
                    try {
                        Pattern p = Pattern.compile("[a-zA-Z0-9]*[.][0-9]*");
                        Matcher m = p.matcher(message.getBody());
                        SavedQuestions sq = db.getSavedQuestions(message.getUserId());
                        StringBuilder outputMsg = new StringBuilder();
                        while (m.find()) {
                            String theme = m.group().split("[.]")[0];
                            int id = Integer.parseInt(m.group().split("[.]")[1]);
                            sq.deleteQuest(theme, id);
                            outputMsg.append("Удален ").append(id).append(" [").append(theme).append("]\n");
                        }
                        db.saveQuestions(sq, message.getUserId());
                        return outputMsg.toString();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return "Ошибка с бд [500]";
            }
        }

                if (message.getBody().matches(":delete [0-9]*.*")){
            try {
                    Pattern p = Pattern.compile(" [0-9]*");
                    Matcher m = p.matcher(message.getBody());
                    ArrayList<Integer> list = new ArrayList<>();
                    SavedQuestions sq = db.getSavedQuestions(message.getUserId());
                    while (m.find()) {
                        int id = Integer.parseInt(m.group().trim());
                        list.add(id);
                        sq.deleteQuest(id);
                    }
                    db.saveQuestions(sq, message.getUserId());
                    return "Удален(-ы) вопросы: " + Arrays.toString(list.toArray());
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        }

        if (message.getBody().equals(":now")){
            if (questions!=null) return questions.getMode().getValue();
            else return "Вопросы еще не инициализированы";
        }
        if (message.getBody().matches(":help [a-zA-Z0-9]*")){
            StringBuilder sb = new StringBuilder();
            for (String modeName: message.getBody().split(" ")){
                if (modeName.equals(":help")) continue;
                QuestMode mode = QuestMode.getMode(modeName);
                if (mode==QuestMode.UNKNOWN){
                    sb.append("Тема с именем ").append(modeName).append(" не найдена");
                } else {
                    sb.append(mode.getValue()).append(":\n");
                    sb.append(mode.getDescription());
                }
                sb.append("\n");
             }
            return sb.toString();
        }
        if (message.getBody().equals(":help")){
            StringBuilder quest = new StringBuilder();
            for (QuestMode q : QuestMode.values()){
                quest.append(q.getValue()).append(" - ").append(q.getDescription()).append("\n");
            }
            return "Режим вопросов.\n" +
                    "Чтобы перейти к теме введите:\n" +
                    ":cm имя_темы\n" +
                    "\nПолучите вопросы с помощью команд:\n" +
                    "next[или n]\n" +
                    "previous[или p]\n" +
                    "random[или r]\n" +
                    "\nСохраняйте вопросы командой:\n" +
                    ":save - она сохранит последний заданный вопрос" +
                    "Подробнее в Wiki:\n" +
                    "https://github.com/AppLoidx/JavaBot/wiki/Режим-quest" +
                    "\n\nСписок доступных тем:\n" +
                    quest;
        }
        if (message.getBody().matches("e|exit|:exit")){
            SessionManager.deleteSession(message.getUserId());
            return "Вы вышли из сессии quest";
        }
        input.writeln(message.getBody());

        while(true){
            if (output.available()){
                out = output.readString();
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return out;

    }

    @Override
    public void setOutput(UserIOStream output) {
        this.output = output;
    }

    @Override
    public void setInput(UserIOStream input) {
        this.input = input;
    }

    @Override
    public UserIOStream getInputStream() {
        return this.input;
    }

    @Override
    public UserIOStream getOutputStream() {
        return this.output;
    }

    @Override
    public void run() {
        questions = new Questions(output);
        questions.init(input);
    }

    private String formatSavedQuests(SavedQuestions sq){
        Map<String, Set<Integer>> map = sq.getAll();
        StringBuilder sb =  new StringBuilder();
        for (String tag: map.keySet()){
            sb.append("\n------------\n").append(tag).append("\n------------\n");
            for (int id: map.get(tag)){
                sb.append(id).append(". ");
                try {
                    sb.append(db.getById(id));
                } catch (SQLException e) {
                    sb.append("ошибка при получении ").append(id);
                }
                sb.append("\n");
            }
        }
        if (sb.toString().equals("")) return "Вопросов нет!";
        return sb.toString();
    }
    private String formatSavedQuests(SavedQuestions sq, String questTag){
        Map<String, Set<Integer>> map = sq.getAll();
        StringBuilder sb =  new StringBuilder();
        for (String tag: map.keySet()){
            if (!tag.equals(questTag)) continue;
            sb.append("\n------------\n").append(tag).append("\n------------\n");
            for (int id: map.get(tag)){
                sb.append(id).append(". ");
                try {
                    sb.append(db.getById(id));
                } catch (SQLException e) {
                    sb.append("ошибка при получении ").append(id);
                }
                sb.append("\n");
            }
            break;
        }
        if (sb.toString().equals("")) return "Вопросов нет!";
        return sb.toString();
    }
}
