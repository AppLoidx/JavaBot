package core.modules.queue;

import core.modules.Time;
import core.modules.queue.exceptions.PersonNotFoundException;

import java.util.TreeMap;

/**
 * @author Arthur Kupriyanov
 */
public class ConsistentQueue extends DatedQueue implements Consistent{
    {
        type = "consistent";
    }

    private int secondQueueStartTime;
    private DatedQueue secondQueue;

    public ConsistentQueue(String name, int time, int secondQueueStartTime) {
        super(name, time);
        this.secondQueue = new DatedQueue(name, time);
        this.secondQueueStartTime = secondQueueStartTime;
    }

    public void setSecondQueueStartTime(int time){
        this.secondQueueStartTime = time;
    }

    public ConsistentQueue(String name, int time) {
        this(name, time, time);
    }

    @Override
    public DatedQueue getSecondQueue(){
        return secondQueue;
    }
    @Override
    public TreeMap<Integer, Person> getFirstQueue() {return this.queue;}
    @Override
    public boolean isSecondQueueStarted(){
        return secondQueueStartTime <= Time.getNowTime();
    }

    @Override
    public TreeMap<Integer, Person> getQueue(){
        if (isSecondQueueStarted()){
            return this.secondQueue.getQueue();
        } else {
            return this.queue;
        }
    }

    /**
     * Добавить персонажа во вторую очередь, уникальность ID сохраняется
     * @see Person
     */
    @Override
    public void addPersonToSecondQueue(Person ... persons){
        this.secondQueue.setFreeId(this.getFreeId());
        if (this.secondQueue.getQueue().isEmpty()){
            this.secondQueue.setCurrentPlace(this.secondQueue.getFreeId());
        }
        this.secondQueue.addPerson(persons);
        this.setFreeId(this.secondQueue.getFreeId());
    }
    @Override
    public void deletePersonFromSecondQueue(int ... ids){
        this.secondQueue.deletePerson(ids);
    }
    @Override
    public Person getPersonFromSecondQueue(int id){
        return this.secondQueue.getPerson(id);
    }
    @Override
    public boolean checkExistOnSecondQueue(int id){
        return this.secondQueue.checkExist(id);
    }
    @Override
    public boolean checkExistOnSecondQueue(int ... ids){
        return this.secondQueue.checkExist(ids);
    }
    @Override
    public void swapOnSecondQueue(int firstId, int secondId) throws PersonNotFoundException{
        this.secondQueue.swap(firstId, secondId);
    }

    @Override
    public int getCurrentPersonID() throws PersonNotFoundException {
        if (isSecondQueueStarted()){
            return this.secondQueue.getCurrentPersonID();
        }
        return super.getCurrentPersonID();
    }

    @Override
    public int getNextPersonID(int step) throws PersonNotFoundException {
        if (isSecondQueueStarted()){
            return this.secondQueue.getNextPersonID(step);
        } else {
            return super.getNextPersonID(step);
        }
    }
    @Override
    public void personPassedToSecondQueue(int id) throws PersonNotFoundException{
        if (checkExist(id)) {
            Person person = getPerson(id);
            this.addPersonToSecondQueue(person);
            super.personPassed(id);
        }
    }
    @Override
    public void personPassed(int id) throws PersonNotFoundException {
        if (isSecondQueueStarted()) {
            this.secondQueue.personPassed(id);
        } else{
            super.personPassed(id);
        }
    }

    @Override
    public String getFormattedQueue() {
        StringBuilder response = new StringBuilder();

        String firstQueue =  new QueueFormatter<TreeMap<Integer, Person>>().format(queue);
        String secondQueue =  new QueueFormatter<TreeMap<Integer, Person>>().format(this.secondQueue.getQueue());

        if (!isSecondQueueStarted()) {
            response.append("------------").append("\nОсновная очередь").append("\n------------\n");
            response.append(firstQueue);
        }
        if (secondQueueStartTime != this.endTime){
            response.append("------------").append("\nДополнительная очередь");
            if (!isSecondQueueStarted()) {
                response.append("\nНачнется в ").append(Time.formattedTime(this.secondQueueStartTime, ":"));
            }
            response.append("\n------------\n");
            response.append(secondQueue);
        }

        return response.toString();
    }

    @Override
    public Stat getStat() {
        if (isSecondQueueStarted()){
            return this.secondQueue.getStat();
        } else {
            return super.getStat();
        }
    }
}
