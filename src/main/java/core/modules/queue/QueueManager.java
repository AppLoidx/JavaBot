package core.modules.queue;

/**
 * @author Arthur Kupriyanov
 */
public class QueueManager {

    public void createQueue(){

    }

    public void saveQueue(Queue queue){}

    public Queue loadQueue(){
        return new SimpleQueue("First queue");
    }

}
