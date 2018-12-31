package core.modules.queue;

import core.modules.Time;

import java.util.TreeMap;

/**
 * @author Arthur Kupriyanov
 */
public class DatedQueue extends SimpleQueue {
    private int endTime;

    DatedQueue(String name, int time) {
        super(name);
        this.endTime = time;
    }

    void setEndTime(int time) {
        this.endTime = time;
    }

    private boolean isEnded(){
        return endTime < Time.getNowTime();
    }

    public void notFullPersonPassed(int id){

    }

    public void addToRepeatQueue(Person person){

    }

    @Override
    public TreeMap<Integer, Person> getQueue() {
        if (endTime < Time.getNowTime()){
            return new TreeMap<>();
        }
        return super.getQueue();
    }
}
