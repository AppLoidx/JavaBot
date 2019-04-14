package core.modules.queuev2;

import com.vk.api.sdk.objects.users.UserXtrCounters;
import core.modules.UsersDB;
import vk.VKManager;

import java.sql.SQLException;
import java.util.List;
import java.util.TreeMap;

/**
 * @author Arthur Kupriyanov
 */
public class QueueFormatter {

    public static String getString(Queue queue){
        TreeMap<Integer, Person> map = queue.getQueue();

        StringBuilder sb = new StringBuilder();

        for (int key : map.keySet()){
            String name;
            try {
                name = new UsersDB().getFullNameByVKID(map.get(key).getId());
            } catch (SQLException e) {
                name = "" + map.get(key).getId();
            }
            sb.append(key).append(". ").append(name).append("\n");
        }

        return sb.toString();
    }

    public static String getString(List<String> list){
        StringBuilder sb = new StringBuilder();
        sb.append("Список очередей").append("\n");
        for (String q : list){
            sb.append(q).append("\n");
        }

        return sb.toString();
    }

}
