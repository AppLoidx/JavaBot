package core.modules.queuev2;

import com.google.gson.annotations.SerializedName;
import core.common.LocaleMath;
import core.modules.queuev2.exceptions.PermissionDeniedException;
import core.modules.queuev2.exceptions.PersonAlreadyExistException;
import core.modules.queuev2.exceptions.PersonNotFoundException;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Arthur Kupriyanov
 */
public class Queue {
    @SerializedName("queue")
    protected final AtomicReference<TreeMap<Integer, Person>> queue = new AtomicReference<>();
    @SerializedName("name")
    private String name;
    @SerializedName("last_place")
    private int lastPlace;
    @SerializedName("super_users_list")
    private Set<Integer> superUsersList;

    public Queue(int id){
        superUsersList = new HashSet<>();
        superUsersList.add(id);
        queue.set(new TreeMap<>());
        lastPlace = -1;
    }

    public boolean isSuperUser(int id){
        return superUsersList.contains(id);
    }

    public void addSuperUser(int id){
        superUsersList.add(id);
    }

    /**
     * Добавление персонажа в очередь
     *
     * Автоматически инкрементирует регистр lastPlace
     * @param userId ИД персонажа
     */
    public void add(int userId){
        if (queue.get().containsValue(new Person(userId))) throw new PersonAlreadyExistException();
        if (queue.get().isEmpty()){
            queue.get().put(0, new Person(userId));
        } else queue.get().put(queue.get().lastKey() + 1, new Person(userId));
    }

    /**
     * Удаление персонажа с очереди
     * @param place место в очереди
     * @see #delete(Person)
     */
    public void delete(int place){
        if (place == queue.get().lastKey()){
            if (queue.get().size() > 1){
                lastPlace = queue.get().lastKey();
            } else {
                lastPlace = -1;
            }
        }
        queue.get().remove(place);
    }

    /**
     * Удаление персонажа с очереди
     * @param p объект персонажа
     * @see #delete(int)
     */
    public void delete(Person p){
        int removeKey = -1;
        for (int key : queue.get().keySet()){
            if (queue.get().get(key).equals(p)){
                removeKey = key;
                break;
            }
        }

        if (removeKey == queue.get().lastKey()){
            if (queue.get().size() > 1){
                lastPlace = queue.get().lastKey();
            } else {
                lastPlace = -1;
            }
        }

        queue.get().remove(removeKey);
    }

    public int getPlace(Person p){
        for (int key : queue.get().keySet()){
            if (queue.get().get(key).equals(p)) return key;
        }
        return -1;
    }

    public Person getPerson(Person person){
        for (Person p : queue.get().values()){
            if (p.equals(person)) return p ;
        }
        return null;
    }
    /**
     * Возвращает персонажа по месту, может вернуть null
     * @param place место персонажа
     * @return либо Person, либо null, если нету такого места
     */
    public Person getPerson(int place){
        return queue.get().get(place);
    }

    /**
     * Меняет местами по месту (не по ИД пользователя)
     *
     */
    private void swap(int place1, int place2){
        Person temp = queue.get().get(place1);
        queue.get().replace(place1, queue.get().get(place2));
        queue.get().replace(place2, temp);

        Person p1 = getPerson(place1);
        Person p2 = getPerson(place2);
        p1.cleanRequestList();
        p2.cleanRequestList();
    }

    private void swap(Person p1, Person p2){
        if (!queue.get().containsValue(p1) || !queue.get().containsValue(p2)) return;
        p1 = getPerson(p1);
        p2 = getPerson(p2);
        int tempKey = -1;
        for (int key : queue.get().keySet()){
            if (queue.get().get(key).equals(p1) || queue.get().get(key).equals(p2)){
                if (tempKey < 0){
                    tempKey = key;
                } else {
                    Person temp = queue.get().get(key);
                    if (queue.get().get(key).equals(p1)){
                        queue.get().replace(key, p2);
                    } else {
                        queue.get().replace(key, p1);
                    }
                    queue.get().replace(tempKey, temp);

                    p1.cleanRequestList();
                    p2.cleanRequestList();

                    break;
                }
            }
        }

    }

    @Deprecated
    public boolean checkedSwap(int place1, int place2){
        if (!queue.get().containsKey(place1) || !queue.get().containsKey(place2)) return false;
        Person firstP = queue.get().get(place1);
        Person secondP = queue.get().get(place1);
        if (!firstP.canSwapWith(secondP)) return false;

        swap(place1, place2);
        return true;
    }

    @Deprecated
    public boolean checkedSwap(Person p1, Person p2) {
        if (queue.get().containsValue(p1) && queue.get().containsValue(p2)) {
            p1 = getPerson(p1);
            p2 = getPerson(p2);
            if (p1.canSwapWith(p2)) {
                swap(p1, p2);
                return true;
            }
        }
        return false;
    }

    /**
     * Если swap не удался - выбрасывает исключение
     * @throws PersonNotFoundException не найден персонаж
     * @throws PermissionDeniedException персонажи не могут обменяться так как это не взаимно
     */
    public void safeSwap(Person p1, Person p2) throws PersonNotFoundException, PermissionDeniedException {

        if (!queue.get().containsValue(p1) || !queue.get().containsValue(p2)) throw new PersonNotFoundException();
        p1 = getPerson(p1);
        p2 = getPerson(p2);
        if (!p1.canSwapWith(p2)) throw new PermissionDeniedException("Persons can't swap");
        swap(p1, p2);
    }

    /**
     * Если swap не удался - выбрасывает исключение
     * @param place1 место в очереди (ключ)
     * @param place2 место в очереди (ключ)
     * @throws PersonNotFoundException не найдено место в очереди
     * @throws PermissionDeniedException персонажи не могут обменяться так как это не взаимно
     */
    public void safeSwap(int place1, int place2) throws PersonNotFoundException, PermissionDeniedException {
        if (!queue.get().containsKey(place1) || !queue.get().containsKey(place2)) throw new PersonNotFoundException();
        if (!queue.get().get(place1).canSwapWith(queue.get().get(place2))) throw new PermissionDeniedException("Persons can't sawp");
        swap(place1, place2);
    }

    public boolean next() {
        if (lastPlace < 0 && !queue.get().isEmpty()) lastPlace = queue.get().firstKey();
        if (lastPlace + 1 == queue.get().size()) return false;
        else {
            lastPlace++;
            return true;
        }
    }

    public Person get(){

        return queue.get().get(lastPlace);
    }

    public boolean isCursorNotNull(){
        return queue.get().containsKey(lastPlace);
    }

    public boolean setLastPlace(int lastPlace){
        if (!queue.get().containsKey(lastPlace)) return false;
        else {
            this.lastPlace = lastPlace;
            return true;
        }
    }

    public boolean containsPerson(Person p){
        return queue.get().containsValue(p);
    }

    /**
     * Тасовка очереди по Фишеру-Йетсу
     *
     */
    public void shuffle(){

        Collection<Person> personsList = queue.get().values();
        Person[] persons = personsList.toArray(new Person[0]);

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

        this.queue.set(newQueue);

    }

    public int size(){
        return queue.get().size();
    }

    List<Person> deleteLast(int count){
        if (queue.get().size() < count) throw new IndexOutOfBoundsException();
        else {
            int delta = queue.get().size() - count;
            List<Person> deletedPersons = new ArrayList<>();
            while (delta > 0){
                int lastKey = queue.get().lastKey();
                deletedPersons.add(queue.get().remove(lastKey));
                delta--;
            }

            return deletedPersons;
        }
    }

    public TreeMap<Integer, Person> getQueue(){
        return queue.get();
    }

    public String getName() { return this.name; }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "Queue{" +
                "\n\tqueue=" + queue +
                ", \n\tlastPlace=" + lastPlace +
                '}';
    }
}
