package core.modules.session;

import core.commands.Mode;
import vk.VKManager;

import java.util.*;

/**
 * @author Arthur Kupriyanov
 */
public class SessionManager {
    private static volatile Map<Integer, Session> sessionMap = new HashMap<>();
    private static Set<Mode> modeList = new HashSet<>();

    public static boolean checkExist(int vkid){
        return sessionMap.containsKey(vkid);
    }

    /**
     * Убирает сессии, которые длятся долго
     * @param timeLimit лимитное время бездействия в минутах
     */
    public static synchronized void cleanSessions(int timeLimit){
        for (int vkid : sessionMap.keySet()){
            if (new Date().getTime() - sessionMap.get(vkid).getLastOperationTime() > timeLimit * 60 * 1000){
                SessionManager.deleteSession(vkid);
                new VKManager().sendMessage("Ваша сессия была автоматически закрыта, так как вы " +
                        "бездействовали " + timeLimit + " минут. Чтобы восстановить сессию - войдите в " +
                        "неё обратно, при этом предыдущая сессия не сохранится.", vkid);
            }
        }
    }

    public static void addSession(int vkid, Session session){
        sessionMap.put(vkid, session);
    }

    public static void deleteSession(int vkid){
        getSession(vkid).getMode().onExit();
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
