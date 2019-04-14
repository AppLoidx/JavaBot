package core.modules.queuev2;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arthur Kupriyanov
 */
public class Person {

    @SerializedName("id")
    private int id;
    @SerializedName("swap_requests")
    private List<Person> swapRequests;

    public Person(int id){
        this.id = id;
        swapRequests = new ArrayList<>();
    }

    public void addSwapRequest(Person p){
        swapRequests.add(p);
    }
    public void cleanRequestList(Person p){
        swapRequests.remove(p);
    }
    public void cleanRequestList(){
        swapRequests.clear();
    }

    /**
     * Если объект метода подал запрос на обмен мест пермонажу <code>p</code>,
     * то возвращает <code>true</code>, иначе <code>false</code>
     * @param p персонаж, наличие которого в заявках нужно проверить
     */
    public boolean checkToSwap(Person p){
        return swapRequests.contains(p);
    }

    /**
     * Проверяет взаимность отправленных заявок
     * @see #checkToSwap(Person)
     */
    public boolean canSwapWith(Person p){
        return checkToSwap(p) && p.checkToSwap(this);
    }

    public int getId(){
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", swapRequests=" + swapRequests +
                '}';
    }
}
