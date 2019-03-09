package core.modules.usersdata;

/**
 * @author Arthur Kupriyanov
 */
public enum Type {
    STUDENT {
        @Override
        public String getType() {
            return "student";
        }
    },
    TEACHER{
        @Override
        public String getType() {
            return "teacher";
        }
    };

    public abstract String getType();
    public static Type getType(String s){
        switch (s){
            case "teacher":
                return Type.TEACHER;
            case "student":
                return Type.STUDENT;
                default:
                    return null;
        }
    }
}
