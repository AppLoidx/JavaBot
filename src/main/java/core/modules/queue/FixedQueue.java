package core.modules.queue;

import core.common.Null;
import core.modules.queue.exceptions.PersonNotFoundException;
import core.modules.queue.exceptions.QueueOverflowException;

import java.util.ArrayList;

/**
 * @author Arthur Kupriyanov
 */
public class FixedQueue extends SimpleQueue implements Fixed{
    {
        type = "fixed";
    }
    private int length = 5;

    private class NullPerson extends Person implements Null{
        NullPerson() {
            super("Место свободно",0);
        }
    }

    public FixedQueue(String name) {
        super(name);
        for (int i = 0; i<this.length;i++){
            addPerson(new NullPerson());
        }
    }

    @Override
    public void addPerson(Person... persons) {
        for (Person person: persons
        ) {
            if (person instanceof Null){
                super.addPerson(person);
            } else {

                try {
                    if (person.getId() != getFreeId()) {
                        person.setId(getFreeId());
                    }
                    this.queue.put(getFirstFreeKey(), person);

                    setFreeId(getFreeId() + 1);
                    this.stat.peopleCount++;
                } catch (QueueOverflowException e) {
                    throw new QueueOverflowException(e.getMessage());
                }
            }
        }
    }
    @Override
    public boolean isFree(int id) throws PersonNotFoundException{
        for (Person person: queue.values()
             ) {
            if (person.getId() == id){
                return person instanceof Null;
            }
        }
        throw new PersonNotFoundException("Персонаж с ID: " + id + " не найден");
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public void setLength(int length) {
        this.length = length;
        int tmpCounter = 1;
        ArrayList<Integer> removeableKeys = new ArrayList<>();

        for (int key: queue.keySet()
             ) {
            if (tmpCounter > length){
                removeableKeys.add(key);
            }
            tmpCounter++;
        }

        for (int key: removeableKeys
             ) {
            queue.remove(key);
        }

        for (int i =0; i<length - tmpCounter + 1;i++){
            addPerson(new NullPerson());
        }
    }

    private int getFirstFreeKey() throws QueueOverflowException{
        for (int key: this.queue.keySet()
             ) {
            if (queue.get(key) instanceof Null){
                return key;
            }
        }
        throw new QueueOverflowException("Не удалось добавить пользователя, очередь заполнена");
    }

}
