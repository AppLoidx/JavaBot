package core.modules.queue;


import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import java.util.TreeMap;

/**
 * @author Arthur Kupriyanov
 */
public class QueueObject {
    /**seat on queue, person class*/
    @Expose
    private TreeMap<Integer, Person> queue;

    /** For statistic */
    @Expose
    private Stat stat;

    /** Current place in queue*/
    @Expose
    private int currentPlace;

    /** Field for saving free ID*/
    @Expose
    private int freeId;

    /** Queue name */
    @Expose
    private String name;

    /** Queue description*/
    @Expose
    private String description;

    @Expose private String type;

    public TreeMap<Integer, Person> getQueue() {
        return queue;
    }

    public Stat getStat() {
        return stat;
    }

    public int getCurrentPlace() {
        return currentPlace;
    }

    public int getFreeId() {
        return freeId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType(){
        return type;
    }

    public static String convertQueueToJSON(Queue queue){
        return new GsonBuilder().create().toJson(queue);
    }
}
