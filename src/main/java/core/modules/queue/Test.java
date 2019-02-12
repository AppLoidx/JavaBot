package core.modules.queue;

/**
 * @author Arthur Kupriyanov
 */
public class Test {
    public static void main(String ... args){
        SimpleQueue sq = new SimpleQueue("new queue");

        sq.addPerson(new Person("Elly"),
                new Person("Sally"),
                new Person("John"),
                new Person("Sallivan"));

        System.out.println(sq.getFormattedQueue());
        sq.swap(1,2);
        System.out.println(sq.getStat().passCount);
        sq.personPassed(sq.getCurrentPersonID());
        sq.personPassed(sq.getCurrentPersonID());

        System.out.println(sq.getStat().passCount);

        sq.addPerson(new Person("Houston"));

        System.out.println(sq.getFormattedQueue());

        ShuffleQueue shq = new ShuffleQueue("Shuffle queue");

        // add persons
        shq.addPerson(new Person("Elly"),
                new Person("Sally"),
                new Person("John"),
                new Person("Sallivan"));

        //output (not shuffle)
        System.out.println(shq.getFormattedQueue());
        shq.shuffle();

        //output shuffled
        System.out.println(shq.getFormattedQueue());

        FixedQueue fq = new FixedQueue("Fixed queue");
        fq.setLength(10);
        fq.addPerson(new Person("Sally"), new Person("Evan"));
        System.out.println(fq.getFormattedQueue());
        fq.addPerson(new Person("Fork"));
        System.out.println(fq.isFree(10));
        System.out.println(fq.isFree(5));
    }
}
