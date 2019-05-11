package core.modules;

import core.common.VoidAction;
import core.common.YesNoAction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class AskYesNo {
    private static Map<String, YesNoAction> list = new HashMap<>();
    public static void addAnswer(String id, boolean isYes){
        if (list.containsKey(id)){
            try {
                if (isYes) list.get(id).yesAction();
                else list.get(id).noAction();
                list.remove(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void answer(String id, YesNoAction action){
        list.put(id, action);
    }
    public static boolean isAnswered(String id){
        return list.containsKey(id);
    }
}
