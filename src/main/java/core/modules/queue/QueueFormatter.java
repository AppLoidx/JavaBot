package core.modules.queue;

import java.util.TreeMap;

/**
 * @author Arthur Kupriyanov
 */
public class QueueFormatter<T> {
    public String format(T queue){
        if (queue instanceof TreeMap) {
            TreeMap treeQueue = (TreeMap) queue;
            if (!treeQueue.isEmpty()) {
                if (treeQueue.lastKey() instanceof Integer && treeQueue.get(treeQueue.lastKey()) instanceof Person) {
                    TreeMap<Integer, Person> checkedQueue = (TreeMap<Integer, Person>) treeQueue;
                    StringBuilder response = new StringBuilder();

                    for (Person person : checkedQueue.values()
                    ) {
                        response.append(person.getName()).append(" id: ").append(person.getId()).append("\n");
                    }

                    return response.toString();
                }
            }
        }

        return "";
    }
}
