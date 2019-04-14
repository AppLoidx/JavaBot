package core.modules.queuev2;

import core.modules.queuev2.exceptions.QueueOverflowException;

import java.util.List;

/**
 * @author Arthur Kupriyanov
 */
public class FixedDoubleQueue extends DoubleQueue{
    private int queueCapacity = 10;

    public FixedDoubleQueue(Queue q1, Queue q2) {
        super(q1, q2);
    }

    public FixedDoubleQueue(DoubleQueue queue){
        super(queue.getFirstQueue(), queue.getSecondQueue());
    }

    @Override
    public void add(int id) throws QueueOverflowException {
        if (firstQueue.size() <= 10) super.add(id);
        else throw new QueueOverflowException("Переполнение очереди");
    }

    public boolean isFree(){
        return firstQueue.size() < queueCapacity;
    }

    public List<Person> setCapacity(int capacity){
        if (capacity < 0) throw new IndexOutOfBoundsException();
        int delta = this.queueCapacity - capacity;
        this.queueCapacity = capacity;
        return firstQueue.deleteLast(delta);
    }
}
