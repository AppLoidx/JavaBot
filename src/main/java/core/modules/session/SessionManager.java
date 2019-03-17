package core.modules.session;

import core.commands.Mode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Arthur Kupriyanov
 */
public class SessionManager {
    private static Map<Integer, Session> sessionMap = new HashMap<>();
    private static Set<Mode> modeList = new HashSet<>();

    public static boolean checkExist(int vkid){
        return sessionMap.containsKey(vkid);
    }

    public static void addSession(int vkid, Session session){
        sessionMap.put(vkid, session);
    }

    public static void deleteSession(int vkid){
        sessionMap.remove(vkid);
    }

    public static void addMode(Mode mode){
        modeList.add(mode);
    }
    public static Set<Mode> getModeList(){
        return modeList;
    }

    public static Session getSession(int vkid){
        return sessionMap.get(vkid);
    }
}
