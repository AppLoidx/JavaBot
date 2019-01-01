package core.modules.queue;

import core.modules.queue.exceptions.PersonNotFoundException;

import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author Arthur Kupriyanov
 */
public class SimpleQueue extends Queue implements Swapable, QueueReturnable<TreeMap<Integer, Person>>, StatReturnable {

    /**<место в очереди, класс персонажа>*/
    protected TreeMap<Integer, Person> queue = new TreeMap<>();

    /** Для статистики */
    private Stat stat = new Stat();

    /** Последний ключ ключа очереди*/
    private int lastKey = 0;

    /** Текущее место в очереди*/
    private int currentPlace = 0;

    /** Поле для сохранения свободного Id*/
    private int freeId = 0;

    /** Имя очереди */
    private String name;

    SimpleQueue(String name){
        this.name = name;
    }

    @Override
    public void saveQueue() throws IOException {
        String path = "src/main/botResources/queue/";
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path + this.name));
        oos.writeObject(this);
    }

    @Override
    public String getQueueName() {
        return name;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public void addPerson(Person... persons) {
        for (Person person: persons
             ) {
            if (person.getId() != freeId){
                person.setId(freeId);
            }
            if (queue.isEmpty()){
                queue.put(freeId, person);
                lastKey = 0;
            }else {
                queue.put(++this.lastKey, person);
            }
            this.freeId++;
            this.stat.peopleCount++;
        }
    }

    @Override
    public void deletePerson(int ... ids) {
        ArrayList<Integer> removableKeys = new ArrayList<>();

        for (int id: ids
             ) {

            for (int key: queue.keySet()
            ) {
                if (queue.get(key).getId() == id){
                    removableKeys.add(key);

                    // Если удаляемый персонаж текущий в очереди, текущим выбираем следующего
                    if (id == currentPlace){
                        this.incrementCurrentPlace();
                    }
                }
            }
        }
        for (int key: removableKeys
             ) {
            queue.remove(key);
        }

        if (queue.isEmpty()){
            freeId = 0;
            currentPlace = 0;
        }
    }

    @Override
    public int getCurrentPersonID() throws PersonNotFoundException {
        if (queue.isEmpty()){
            throw new PersonNotFoundException("Персонаж с указанным ID не найден");
        }
        return queue.get(currentPlace).getId();
    }

    @Override
    public int getNextPersonID(int step) throws PersonNotFoundException {

        if (queue.containsKey(currentPlace + step)){
            return queue.get(currentPlace + step).getId();
        } else{
            throw new PersonNotFoundException("Не найден персонаж с таким ID");
        }
    }

    @Override
    public Person getCurrentPerson() throws PersonNotFoundException {
        if (!queue.isEmpty()){
            return queue.get(currentPlace);
        } else{
            throw new PersonNotFoundException("Очередь пустая");
        }
    }

    @Override
    public Person getPerson(int id) throws PersonNotFoundException {
        for (Person p: queue.values()
             ) {
            if (p.getId() == id){
                return p;
            }
        }

        throw new PersonNotFoundException("Персонаж не найден");
    }

    @Override
    public void personPassed(int id) {
        queue.remove(currentPlace);
        this.incrementCurrentPlace();
        this.stat.passCount++;

    }

    @Override
    public boolean checkExist(int id) {
        for (Person person: queue.values()
             ) {
            if (person.getId() == id){
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean checkExist(int ... ids) {
        for (int id: ids
             ) {
            if (!checkExist(id)){
                return false;
            }
        }

        return true;
    }

    /**
     * Меняет местами в очереди по идентификаторам
     * @param firstId идентификатор первого персонажа
     * @param secondId идентификатор второго персонажа
     * @throws PersonNotFoundException бросается если персонаж не найден по <code>id</code>
     */
    @Override
    public void swap(int firstId, int secondId) throws PersonNotFoundException {
        Person firstPerson = null;
        Integer firstPersonKey = null;

        Person secondPerson = null;
        Integer secondPersonKey = null;

        for (Integer key: queue.keySet()
             ) {
            Person person = queue.get(key);
            int personId = person.getId();
            if (personId == firstId){
                firstPerson = person;
                firstPersonKey = key;
            }
            if (personId == secondId) {
                secondPerson = person;
                secondPersonKey = key;
            }
        }

        if (firstPerson != null && secondPerson != null) {
            queue.replace(firstPersonKey, secondPerson);
            queue.replace(secondPersonKey, firstPerson);
        } else{
            throw new PersonNotFoundException("Искомый персонаж не найден по ID");
        }
    }

    public int getFreeId(){
        return this.freeId;
    }

    private void incrementCurrentPlace(){
        for (int key: queue.keySet()
             ) {
            if (key > this.currentPlace){
                this.currentPlace = key;
                break;
            }
        }
    }

    @Override
    public TreeMap<Integer, Person> getQueue() {
        return queue;
    }

    @Override
    public Stat getStat() {
        return stat;
    }

}
