package core.modules.queue;

/**
 * @author Arthur Kupriyanov
 */
public abstract class Queue{

    /**
     * @return имя очереди
     */
    public abstract String getQueueName();

    /**
     * @return описание очереди
     */
    public abstract String getDescription();

    /**
     * Проверка на содержимое очереди
     * @return True - если очередь пустая
     */
    public abstract boolean isEmpty();

    /**
     * Добавляет персонажей в очередь
     * @param persons добавляемые персонажи
     */
    public abstract void addPerson(Person... persons);

    /**
     * Удаляет персонажей по <code>id</code>
     * @param ids идентификаторы персонажей
     */
    public abstract void deletePerson(int ... ids);

    /**
     * @return <code>id</code> текущего в очереди персонажа
     */
    public abstract int getCurrentPersonID();

    /**
     * @param step шаг через котрорый будет выведен персрнаж<br>
     *             0 - текущий, 1 - второй и тд..( место в очереди)
     * @return <code>id</code> указанного места в очереди персонажа
     */
    public abstract int getNextPersonID(int step);

    /**
     * Возвращает объект текущего в очереди персонажа
     * @return объект текущего в очереди персонажа
     */
    public abstract Person getCurrentPerson();

    /**
     * Получение персонажа по идентификатору
     * @param id идентификатор персонажа
     * @return объект персонажа
     */
    public abstract Person getPerson(int id);

    /**
     * Прохождение кем-то очереди
     * @param id идентификатор персонажа
     */
    public abstract void personPassed(int id);

    /**
     * Проверка на существование указанного <code>id</code> в очереди
     * @param id идентификатор персонажа
     * @return True -- если указанный <code>id</code> есть в очереди
     */
    public abstract boolean checkExist(int id);

    /**
     * Перегрузка для {@link #checkExist(int)} со множеством <code>id</code>
     * @param ids идентификаторы персонажей
     * @return True -- если все указанные id существуют
     */
    public abstract boolean checkExist(int ... ids);

}
