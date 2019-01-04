package core.modules.queue;

import core.modules.Time;

import java.util.TreeMap;

/**
 * @author Arthur Kupriyanov
 */
public class DatedQueue extends SimpleQueue {
    protected int endTime;

    public DatedQueue(String name, int time) {
        super(name);
        this.endTime = time;
    }

    void setEndTime(int time) {
        this.endTime = time;
    }

    private boolean isEnded(){
        return endTime < Time.getNowTime();
    }

    @Override
    public TreeMap<Integer, Person> getQueue() {
        if (endTime < Time.getNowTime()){
            TreeMap<Integer, Person> res =  new TreeMap<>();
            res.put(0, new Person("Очередь закончилась",666));
            return res;
        }
        return super.getQueue();
    }

    @Override
    public String getFormattedQueue() {
        String endMessage = "Очередь закончится в " + Time.formattedTime(endTime,":");
        return super.getFormattedQueue() + "\n" + endMessage;
    }
}
