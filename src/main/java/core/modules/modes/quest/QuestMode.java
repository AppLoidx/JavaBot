package core.modules.modes.quest;

/**
 * @author Arthur Kupriyanov
 */
public enum QuestMode {
    DEFAULT("default"){
        @Override
        public String getDescription() {
            return "Стартовое окно";
        }
    },
    COLLECTIONS("collections"){
        @Override
        public String getDescription() {
            return "Вопросы по теме коллекции";
        }
    },
    GENERICS("generics"){
        @Override
        public String getDescription() {
            return "Вопросы из Generics и его употребления в коллекциях";
        }
    },
    UNKNOWN("unknown");

    private final String value;

    QuestMode(String value){
        this.value = value;
    }

    /**
     * Поулчить значение QuestMode по строковому представлению
     * @param value строковое представление мода
     * @return QuestMode с соответсвующим строковым представлением.
     * Если такого не нашлось, то QuestMode.UNKNOWN
     */
    public static QuestMode getMode(String value){
        for(QuestMode mode : QuestMode.values()){
            if (mode.getValue().equals(value)) return mode;
        }

        return QuestMode.UNKNOWN;
    }

    public String getValue() {
        return value;
    }

    public String getDescription(){
        return null;
    }

}
