package core.modules.queue;

import java.util.TreeMap;

/**
 * @author Arthur Kupriyanov
 */
public class Main {
    public static void main(String ... args){
        ShuffleQueue sq = new ShuffleQueue("queue");

        sq.addPerson(new Person("Jojo", sq.getFreeId()),
                new Person("Sally", sq.getFreeId()),
                new Person("Arch", sq.getFreeId()),
                new Person("Lucas", sq.getFreeId()));

        printQueue(sq.getQueue());
        sq.shuffle();
        printQueue(sq.getQueue());

    }
    private static void printQueue(TreeMap<Integer, Person> queue){
        System.out.println(" ---------------");
        for (int k: queue.keySet()
             ) {

            System.out.println(k + " " + queue.get(k).getName() + " id: " + queue.get(k).getId());
        }
    }
}
