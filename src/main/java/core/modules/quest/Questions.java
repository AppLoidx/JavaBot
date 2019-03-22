package core.modules.quest;

import core.modules.session.UserIOStream;

import java.sql.SQLException;
import java.util.Map;

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

        public void drop(){
            exitStatus = true;
        }

        public void setId(int id){
            this.lastId = id;
        }

        private void systemCall(String input){
            // TODO: Обработка систесных вызовов [дополнить]
            if (input.matches(":cm .*")){
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
                String input;
                while(true){
                    if (inputStream.available()){
                        input = inputStream.readString();
                        break;
                    }
                }
                if (isSystemCall(input)) systemCall(input);

                switch (input){
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
                    case "br":
                        branch();
                        break;
                }
            } while (!exitStatus);
        }

        private void changeMode(String input){
            QuestMode mode = QuestMode.getMode(input);
            if (mode == QuestMode.UNKNOWN) outputStream.writeln("Неизвестный режим");
            loadData();
            this.mode = mode;
            this.lastId = 0;
        }

        private void next(){
            switch (mode){
                case GENERICS:
                    nextGenerics();
                case COLLECTIONS:
                    nextCollections();
                case DEFAULT:
                    defaultCall();
            }
        }

        private void previous(){
            switch (mode){
                case GENERICS:
                    nextGenerics();
                case COLLECTIONS:
                    nextCollections();
                case DEFAULT:
                    defaultCall();
            }
        }

        private void random(){
            // TODO: write random
        }

        private void branch(){
            // TODO: write branch
        }
        private void nextGenerics(){
            // TODO: write next generics
        }

        private void nextCollections(){
            // TODO: write next collections
        }

        private void defaultCall(){
            // TODO: write default
        }
}
