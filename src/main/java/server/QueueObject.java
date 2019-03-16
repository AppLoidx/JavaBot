package server;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import core.modules.queue.Person;
import core.modules.queue.Queue;
import core.modules.queue.Stat;

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
        return new Gson().newBuilder().create().toJson(queue);
    }
}
