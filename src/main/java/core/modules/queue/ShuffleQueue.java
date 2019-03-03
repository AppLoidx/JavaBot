package core.modules.queue;

import core.common.LocaleMath;

import java.util.*;

/**
 * @author Arthur Kupriyanov
 */
public class ShuffleQueue extends SimpleQueue implements Shuffleable {
    {
        type = "shuffle";
    }
    public ShuffleQueue(String name) {
        super(name);
    }


    /**
     * Тасовка персонажей внутри очереди
     * Используется алгоритм тасовки Фишера-Йетса
     */
    public void shuffle(){

        Collection<Person> personsList = queue.values();
        Person[] persons = personsList.toArray(new Person[personsList.size()]);

        for(int i=1;i<persons.length;i++){
            int r = LocaleMath.randInt(0, i);
            Person tmp = persons[r];
            persons[r] = persons[i];
            persons[i] = tmp;
        }

        TreeMap<Integer, Person> newQueue = new TreeMap<>();
        Integer key = 0;
        for (Person person: persons
             ) {
            newQueue.put(key, person);
            key++;
        }

        this.queue = newQueue;
    }

}
