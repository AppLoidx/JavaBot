package core.modules.queuev2;

import core.modules.queuev2.exceptions.PersonAlreadyExistException;

/**
 * Представляет собой двойную очередь, где участники могут
 * пройти во вторую очередь
 *
 * @author Arthur Kupriyanov
 */
public class DoubleQueue {
    Queue firstQueue;
    private Queue secondQueue;

    private boolean onFirstQueue = true;

    public DoubleQueue(Queue q1, Queue q2){
        firstQueue = q1;
        secondQueue = q2;
    }

    /**
     * Перемещение курсора на следующего персонажа
     * @return истина, если курсор сменился
     */
    public boolean next(){
        if (onFirstQueue){
            boolean state = firstQueue.next();
            onFirstQueue = state;
            return state;
        } else {
            return secondQueue.next();
        }
    }

    /**
     * Перемещает текущего пользователя во вторую очередь
     *
     * Не вызывает {@link #next()}
     * @return результат опреации
     */
    public boolean branchToSecondQueue(){
        if (onFirstQueue && firstQueue.isCursorNotNull()){
            Person p = firstQueue.get();
            secondQueue.add(p.getId());
            return true;
        } else {
            return false;
        }
    }

    public Person get(){
        if (onFirstQueue){
            return firstQueue.get();
        } else {
            return secondQueue.get();
        }
    }

    /**
     *
     * @param id номер добавляемого персонажа
     */
    public void add(int id){
        if (firstQueue.containsPerson(new Person(id))) throw new PersonAlreadyExistException();
        firstQueue.add(id);
    }

    public void addPersonToSecondQueue(Person p){
        secondQueue.add(p.getId());
    }

    public void addPersonToSecondQueue(int id){
        secondQueue.add(id);
    }

    public Queue getFirstQueue() { return firstQueue;}
    public Queue getSecondQueue() { return secondQueue;}

    @Override
    public String toString() {
        return "DoubleQueue{" +
                "firstQueue=" + firstQueue +
                ", secondQueue=" + secondQueue +
                ", onFirstQueue=" + onFirstQueue +
                '}';
    }
}
