package core.modules.queuev2;

import core.modules.queuev2.exceptions.QueueOverflowException;

import java.util.List;

/**
 * @author Arthur Kupriyanov
 */
public class FixedQueue extends Queue {

    private int capacity = 15;

    public FixedQueue(int id) {
        super(id);
    }

    @Override
    public void add(int userId){
        if (capacity < queue.get().size()) throw new QueueOverflowException("Переполнение очереди");
        super.add(userId);
    }

    /**
     * Задает новый размер очереди, при этом удаляет лишних участников (последних)
     * @param capacity новый размер
     * @throws IndexOutOfBoundsException если введено недопустимое значение capacity (less than 0)
     */
    public List<Person> setCapacity(int capacity){
        if (capacity < 0) throw new IndexOutOfBoundsException();
        int delta = this.capacity - capacity;
        this.capacity = capacity;
        return deleteLast(delta);
    }
}
