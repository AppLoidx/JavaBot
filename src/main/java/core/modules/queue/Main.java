package core.modules.queue;

import core.modules.Time;
import core.modules.queue.exceptions.PersonNotFoundException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeMap;

/**
 * @author Arthur Kupriyanov
 */
public class Main {
    public static void main(String ... args){

        FixedQueue fq = new FixedQueue("Fixed Queue");
        System.out.println(fq.getFormattedQueue());
        fq.setLength(10);
        fq.addPerson(new Person("Luwis"),new Person("Charlie"),new Person("Brodie"));
        System.out.println(fq.getFormattedQueue());
        fq.setLength(2);
        System.out.println(fq.getFormattedQueue());
        fq.setLength(3);
        System.out.println(fq.getFormattedQueue());
        fq.swap(10,13);
        fq.setLength(5);
        try {
            System.out.println(fq.isFree(5));
        } catch (PersonNotFoundException e){
            System.out.println(e.getMessage());
        }
        fq.addPerson(new Person("Luigi"));
        System.out.println(fq.isFree(14));
        System.out.println(fq.getFormattedQueue());
    }
    private static void printQueue(TreeMap<Integer, Person> queue){
        System.out.println(" ---------------");
        for (int k: queue.keySet()
             ) {

            System.out.println(k + " " + queue.get(k).getName() + " id: " + queue.get(k).getId());
        }
    }

}
