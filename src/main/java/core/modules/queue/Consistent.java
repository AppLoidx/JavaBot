package core.modules.queue;

import core.modules.queue.exceptions.PersonNotFoundException;

import java.util.TreeMap;

/**
 * @author Arthur Kupriyanov
 */
public interface Consistent {

    void setSecondQueueStartTime(int time);
    DatedQueue getSecondQueue();
    TreeMap<Integer, Person> getFirstQueue();
    boolean isSecondQueueStarted();
    void addPersonToSecondQueue(Person ... persons);
    void deletePersonFromSecondQueue(int ... ids);
    Person getPersonFromSecondQueue(int id);
    boolean checkExistOnSecondQueue(int id);
    boolean checkExistOnSecondQueue(int ... ids);
    void swapOnSecondQueue(int firstId, int secondId) throws PersonNotFoundException;
    void personPassedToSecondQueue(int id) throws PersonNotFoundException;



}
