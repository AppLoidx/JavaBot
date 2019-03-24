package core.modules.quest;

import core.common.LocaleMath;
import core.modules.session.UserIOStream;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Системный вызов начинается с спецсимвола <code>:</code>
 * <blockquote>
 *     <code>
 *         :this_is_system_call
 *     </code>
 * </blockquote>
 *
 *
 * Остальные команды с простых слов в нижнем регистре<br>
 * Зависимость от регистрров не предусмотрена
 * @author Arthur Kupriyanov
 */
public class Questions {
        private static QuestionsDB db = new QuestionsDB();  // база данных
        private UserIOStream outputStream;                  // поток вывода

    private boolean exitStatus = false;                 // флаг выхода
    private QuestMode mode = QuestMode.DEFAULT;         // регистр мода
    private int lastId;                                 // номер последнего вопроса

    public boolean getStatus() {
        return exitStatus;
    }

    public QuestMode getMode() {
        return mode;
    }

    public int getLastId() {
        return lastId;
    }

        private Map<Integer, String> questions;


        public Questions(UserIOStream outputStream){
            this.outputStream = outputStream;
        }

        private void loadData(){
            try {
                this.questions = db.getQuestions(mode.getValue());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private void drop(){
            exitStatus = true;
        }

        public void setId(int id){
            this.lastId = id;
        }

        private void systemCall(String input){
            // TODO: Обработка систесных вызовов [дополнить]
            if (input.matches(":cm [a-zA-Z0-9]*")){
                changeMode(input);
            }
            if (input.equals(":exit")){
                drop();
            }
        }

        private boolean isSystemCall(String input){
            return input.matches(":.*");
        }

        public void init(UserIOStream inputStream){
            do {
                String input = read(inputStream);
                if (isSystemCall(input)){
                    systemCall(input);
                    continue;
                }
                if (input.equals("exit") || input.equals("e")){
                    drop();
                }
                switch (input){
                    case "help":
                        help();
                        break;
                    case "next":
                    case "n":
                        next();
                        break;
                    case "previous":
                    case "p":
                        previous();
                        break;
                    case "random":
                    case "r":
                        random();
                        break;
                    default:
                        if (input.matches("br [0-9]*")){
                            int key = Integer.parseInt(input.split(" ")[1]);
                            if (mode==QuestMode.DEFAULT){
                                try {
                                    questions = db.getAllQuestions();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (questions!=null) {
                                if (questions.containsKey(key)) {
                                    outputStream.writeln(key + ". " + questions.get(key));
                                    lastId = key;
                                } else outputStream.writeln("Вопроса с номером " + key +
                                        " не существует!");
                            }else {
                                outputStream.writeln("Вопросов нет");
                            }

                            if (mode==QuestMode.DEFAULT) questions=null;
                        } else outputStream.writeln("Неизвестная команда " + input);
                }
            } while (!exitStatus);
        }

        private void changeMode(String input){
            QuestMode mode = QuestMode.getMode(input.split(" ")[1]);
            if (mode == QuestMode.UNKNOWN){
                outputStream.writeln("Неизвестный режим");
                return;
            }
            this.mode = mode;
            loadData();
            outputStream.writeln("Вы перешли на тему: " + mode.getValue() +
                    "\n Загружено: " + this.questions.keySet().size() + " вопросов");
            this.lastId = 0;
        }

        private void next(){
            switch (mode){
                case GENERICS:
                    nextGenerics();
                    break;
                case COLLECTIONS:
                    nextCollections();
                    break;
                case DEFAULT:
                    defaultCall();
            }
        }

        private void previous(){
            switch (mode){
                case GENERICS:
                    previousGenerics();
                    break;
                case COLLECTIONS:
                    previousCollections();
                    break;
                case DEFAULT:
                    defaultCall();
            }
        }

        private void random(){
            if (mode==QuestMode.DEFAULT){
                try {
                    questions = db.getAllQuestions();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (questions==null){
                outputStream.writeln("Вопросов нет");
            }
            Set<Integer> keySet = questions.keySet();
            int r = LocaleMath.randInt(0, keySet.size());
            Iterator<Integer> iter = questions.keySet().iterator();
            int count = 0;
            while (iter.hasNext()){
                int key = iter.next();
                if (count == r){
                    outputStream.writeln(key+". "+questions.get(key));
                    lastId = key;
                    break;
                } else {
                    count++;
                }
            }

            if (mode==QuestMode.DEFAULT) questions = null;
        }

        private void nextGenerics(){
            defaultNext();
        }

        private void nextCollections(){
            defaultNext();

        }

        private void defaultNext(){
            boolean sended = false;
            for (int key : questions.keySet()){
                if (key > lastId){
                    outputStream.writeln(key+". "+questions.get(key));
                    lastId = key;
                    sended = true;
                    break;
                }
            }
            if (!sended){
                if (questions.isEmpty()){
                    outputStream.writeln("Вопросы с таким тегом еще не добавлены");
                    return;
                }
                lastId = questions.keySet().iterator().next();
                outputStream.writeln(questions.get(lastId));
            }
        }

        private void defaultPrevious(){
            Iterator<Integer> iterator = questions.keySet().iterator();
            boolean first = true;
            int prevKey = 0;
            while(iterator.hasNext()){
                int key=iterator.next();
                if (first){
                    prevKey = key;
                    first = false;
                }
                if (key >= lastId){
                    outputStream.writeln(key+". "+questions.get(prevKey));
                    lastId = prevKey;
                    break;
                } else {
                    prevKey = key;
                }
            }

        }
        private void defaultCall(){
            String list = "";
            for (QuestMode mode : QuestMode.values()){
                if (mode == QuestMode.UNKNOWN || mode==QuestMode.DEFAULT) continue;
                list += mode.getValue() + "\n";
            }
            outputStream.writeln("Чтобы перейти к какой-нибудь тематике, используйте :\n:cm имя_темы\n\n" +
                    "На данный момент доступны:\n\n" + list);
        }

        private void help(){
            outputStream.writeln("Используйте команды-переходы:\nprevious\nnext\nrandom" +
                    "\nbr\n\nСохраняйте и просматривайте через команды:\n" +
                    ":save\n:my\nПодробнее:\nhttps://github.com/AppLoidx/JavaBot/wiki/Режим-quest");
        }
        private void previousCollections(){
            defaultPrevious();
        }
        private void previousGenerics(){
            defaultPrevious();
        }

        private String read(UserIOStream stream){
            while(true){
                if (stream.available()){
                    return stream.readString();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
}
