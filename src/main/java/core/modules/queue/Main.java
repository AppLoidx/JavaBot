package core.modules.queue;

import core.modules.Time;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeMap;

/**
 * @author Arthur Kupriyanov
 */
public class Main {
    public static void main(String ... args){

        ConsistentQueue cq = new ConsistentQueue("queue", Time.parseTime("23:50"), Time.parseTime("23:40"));

        cq.addPerson(new Person("Sally"),
                new Person("Jobby"),
                new Person("Sallivan"),
                new Person("Hobkins"));
        cq.addPersonToSecondQueue(new Person("Job"), new Person("Ellie"));
        System.out.println(cq.getFormattedQueue());
        //cq.personPassedToSecondQueue(0);

        System.out.println(cq.getFormattedQueue());

    }
    private static void printQueue(TreeMap<Integer, Person> queue){
        System.out.println(" ---------------");
        for (int k: queue.keySet()
             ) {

            System.out.println(k + " " + queue.get(k).getName() + " id: " + queue.get(k).getId());
        }
    }

}
