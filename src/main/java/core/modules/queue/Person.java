package core.modules.queue;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Класс персонажа для объектов в очереди
 *
 * @author Arthur Kupriyanov
 */
public class Person implements Serializable {
    /** Имя персонажа*/
    @Expose
    private String name;
    /** Номер персонажа, однозначно идентифицирующий его*/
    @Expose
    private int id;
    @Expose
    private int vkid;
    /**
     *
     * @param name имя персонажа
     * @param id номер персонажа, однозначно идентифицирующий его
     */
    public Person(String name, int id, int vkid){
        this.name = name;
        this.id = id;
        this.vkid = vkid;
    }
    public Person(String name, int vkid){
        this.name = name;
        this.id = -1;
        this.vkid = vkid;
    }

    public int getId() {
        return id;
    }
    void setId(int id){ this.id = id;}
    public String getName() {
        return name;
    }


    public int getVKID(){
        return this.vkid;
    }

    /**
     * Генерируется из {@link String#hashCode()} имени и номера {@link #id}
     * @return hashCode
     */
    @Override
    public int hashCode() {
        return name.hashCode() * 31 + id;
    }

    /**
     * Объекты равны в том случае, если их имена и номера равны
     * @param obj сравниваемый объект
     * @return True, если равны поля {@link #name} и {@link #id} и объект является наследником Person или им самим
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person){
            return ((Person) obj).getName().equals(this.name) && ((Person) obj).getId() == this.getId();
        }

        return false;
    }
}
