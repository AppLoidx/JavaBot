package core.modules.vkSDK.request.keyboard;

/**
 * @author Arthur Kupriyanov
 */
public enum KeyboardColor {
    PRIMARY{
        @Override
        public String getValue() {
            return "primary";
        }
    },
    DEFAULT{
        @Override
        public String getValue() {
            return "default";
        }
    },
    NEGATIVE{
        @Override
        public String getValue() {
            return "negative";
        }
    },
    POSITIVE{
        @Override
        public String getValue() {
            return "positive";
        }
    };

    public static KeyboardColor getEnum(String value){
        switch (value){
            case "positive":
                return KeyboardColor.POSITIVE;
            case "negative":
                return KeyboardColor.NEGATIVE;
            case "primary":
                return KeyboardColor.PRIMARY;
                default:
                    return KeyboardColor.DEFAULT;
        }
    }

    public String getValue(){
        return null;
    }
}
