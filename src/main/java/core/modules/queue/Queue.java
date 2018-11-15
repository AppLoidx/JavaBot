package core.modules.queue;

import core.common.JSONReader;
import core.common.LocaleMath;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;

public class Queue {

    /**
     * Создает очередь из списка <code>groupFilename</code> в файл с именем <code>path</code>, находящийся
     * в <code>path</code>, начиная с номера (id) <code>byId</code>
     *
     * @param path путь по которому сохраняется файл
     * @param name имя файла с очередью
     * @param groupFilename имя файла со списком группы
     * @param byId создает очередь начиная с человека с этим номером. Есть реализация без этого параметра
     *             {@link #createQueue(String, String, String)}
     * @throws IOException при работе с чтением файлов
     * @throws ParseException при работе с JSON
     */
    public static void createQueue(String path, String name, String groupFilename, int byId) throws IOException, ParseException {
        TreeMap<Integer, Person> queue = generateQueue(groupFilename, byId);

        writeQueue(queue, path+"\\"+name);
    }

    /**
     * Переопределённая функция {@link #createQueue(String, String, String, int)}. Указывается рандомный параметр
     * <code>byId</code> через {@link LocaleMath}
     */
    public static void createQueue(String path, String name, String groupFilename) throws IOException, ParseException {
        JSONObject data = JSONReader.getJSONObject(groupFilename);
        JSONArray persons = (JSONArray) data.get("Persons");

        int maxValue = getLength(persons);

        createQueue(path,name,groupFilename, LocaleMath.randInt(1, maxValue));
    }

    /**
     * Читает файл с очередью
     * @param filename имя файла с очередью (должен быть соблюден определенный шаблон)
     * @return Map очереди, где ключи место в очереди
     * @throws IOException при работе с файлами
     */
    public static TreeMap<Integer, Person> readQueue(String filename) throws IOException {
        TreeMap<Integer, Person> queue = new TreeMap<>();

        BufferedReader reader = new BufferedReader( new FileReader(filename));
        String line;
        int queueCount = 1;
        while( ( line = reader.readLine() ) != null ) {
            String[] person = line.split(" ");
            String name = person[0]+" "+person[1];
            int id = Integer.valueOf(person[2]);
            Person.Status personStatus = Person.Status.WAITING; // default value

            for (Person.Status status: Person.Status.values()
            ) {
                if (person[3].equals(status.toString())){
                    personStatus = status;
                }
            }
            queue.put(queueCount,
                    new Person(name,id, queueCount, personStatus));
            queueCount++;
        }
        return queue;
    }

    /**
     * Класс для взаимодействия с очередью
     */
    public static class QueueEdit {
        String filenameWithQueue;
        TreeMap<Integer, Person> queue;
        /**
         * @param filenameWithQueue имя файла с очередью группы
         * @throws IOException при работе с файлами
         */

        public QueueEdit(String filenameWithQueue) throws IOException {
            this.filenameWithQueue = filenameWithQueue;
            queue = readQueue(filenameWithQueue);

        }

        /**
         * Меняет местами двух человек из очереди
         * @param firstId номер первого
         * @param secondId номер второго
         */
        boolean swap(int firstId, int secondId) throws IOException {
            Person firstPerson = new Person();
            Person secondPerson = new Person();

            int firstPersonKey = -1;
            int secondPersonKey = -1;

            if (idExistInQueue(queue, firstId) && (idExistInQueue(queue, secondId))) {
                for (int key : queue.keySet()
                ) {
                    if (queue.get(key).getId() == firstId) {
                        firstPerson = queue.get(key);
                        firstPersonKey = key;
                    }
                    if (queue.get(key).getId() == secondId) {
                        secondPerson = queue.get(key);
                        secondPersonKey = key;
                    }
                }
                queue.replace(firstPersonKey,secondPerson);
                queue.replace(secondPersonKey, firstPerson);
                return true;
            }else{
                return false;
            }
        }

        /**
         * Удаляет объект из очереди
         *
         * @param id номер удаляемого объекта
         * @return <code>true</code> - успешное удаление<br> <code>false</code> - ошибка удаления
         */
        public boolean delete(int id) throws IOException {
            if (!idExistInQueue(queue, id)){
                return false;
            }
            id = getQueuePlaceById(queue,id);
            int length = queue.lastKey();

            for(int i=id;i<length;i++){
                queue.replace(i, queue.get(i+1));
            }
            queue.remove(length);
            writeQueue(queue,filenameWithQueue);
            return true;
        }

        /**
         * Добавляет объект в очередь, не нарушая последовательность очрерди.
         * @param name имя добавляемого объекта
         * @param id номер добавляемого объекта
         * @return <code>true</code> - успешное добавление<br> <code>false</code> - ошибка добавления
         */
        public boolean add(String name, int id) throws IOException {
            Person newPerson = new Person(name,id,queue.lastKey()+1, Person.Status.WAITING);
            if (personExistInQueue(queue,newPerson)){
                return false;
            }

            queue.put(queue.lastKey()+1,newPerson);
            writeQueue(queue,filenameWithQueue);
            return true;
        }

        public void nextStage(){
            setNextStatusByQueuePlace(getLastPassingPersonQueuePlace());
            setNextStatusByQueuePlace(getFirstWaitingPersonQueuePlace());
        }

        int getFirstWaitingPersonQueuePlace(){
            for (int key: queue.keySet()
                 ) {
                if (queue.get(key).getStatus() == Person.Status.WAITING){
                    return key;
                }
            }
            return 0;
        }

        int getLastPassingPersonQueuePlace(){
            for (int key:queue.keySet()
                 ) {
                if (queue.get(key).getStatus() == Person.Status.PASSING){
                    if (key == queue.lastKey()){
                        return key;
                    }else if(queue.get(key+1).getStatus() != Person.Status.PASSING){
                        return key;
                    }
                }
            }
            return 0;
        }

        public boolean setNextStatusById(int id){
            return setNextStatusByQueuePlace(getQueuePlaceById(queue, id));
        }

        public boolean setNextStatusByQueuePlace(int queuePlace){
            if (!queue.containsKey(queuePlace)){ return false;}

            Person person = queue.get(queuePlace);
            Person.Status status = person.getStatus();

            switch (status){
                case WAITING:
                    status = Person.Status.PASSING;
                    break;
                default:
                    status = Person.Status.PASSED;
            }
            Person changedPerson = new Person(person.getName(), person.getId(),person.queueCount,status);
            queue.replace(queuePlace, changedPerson);
            try {
                writeQueue(queue, filenameWithQueue);
            } catch (IOException e) {
                queue.replace(queuePlace, person);  // В случае ошибки восстанавливаем очередь
                e.printStackTrace();
            }
            return false;
        }
    }

    private static void writeQueue(TreeMap<Integer, Person> queue, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        for (int key: queue.keySet()
        ) {
            Person person = queue.get(key);
            writer.write(person.getName() +" "+ person.getId() +" "+ person.getStatus().toString()+ "\n");
        }
        writer.flush();
        writer.close();
    }

    private static TreeMap<Integer, Person> generateQueue(String filename, int byId) throws IOException, ParseException {
            byId--;

            TreeMap<Integer, Person> personTreeMap= new TreeMap<>();

            JSONObject data = JSONReader.getJSONObject(filename);
            JSONArray persons = (JSONArray) data.get("Persons");

            int count = getLength(persons);

        for (Object person : persons) {
            JSONObject personObject = (JSONObject) person;
            int personId = Integer.valueOf(personObject.get("id").toString());

            int queuePlace;
            if (byId < personId) {
                queuePlace = personId - byId;
            } else {
                queuePlace = count - byId + personId;
            }

            Person personClass = new Person(
                    personObject.get("name").toString(),
                    Integer.valueOf(personObject.get("id").toString()),
                    queuePlace,
                    Person.Status.WAITING);

            personTreeMap.put(personClass.getQueueCount(), personClass);
        }
        return personTreeMap;
    }

    private static TreeMap<Integer, Person> generateQueue(String filename) throws IOException, ParseException {
        return generateQueue(filename, 1);
    }

    private static int getQueuePlaceById(TreeMap<Integer, Person> queue, int id){
        if (idExistInQueue(queue, id)) {
            for (int key : queue.keySet()) {
                if (queue.get(key).getId() == id) {
                    return key;
                }
            }
        }
        return 0;
    }

    private static boolean idExistInQueue(TreeMap<Integer, Person> queue, int id){
        for (int key:queue.keySet()
        ) {
            if (queue.get(key).getId() == id){
                return true;
            }
        }
        return false;
    }

    private static boolean personExistInQueue(TreeMap<Integer, Person> queue, Person person){
        for (int key:queue.keySet()
             ) {
            if (queue.get(key).equals(person)){
                return true;
            }
        }
        return false;
    }

    private static int getLength(JSONArray array){
        Iterator iterator = array.iterator();
        int length = 0;
        while (iterator.hasNext()){
            iterator.next();
            length++;
        }
        return length;
    }

    public static class Person{

        Person(){}
        Person(String name, int id){
            setName(name);
            setId(id);
        }
        Person(String name, int id, int queueCount,Status status){
            setName(name);
            setId(id);
            setQueueCount(queueCount);
            setStatus(status);
        }
        enum Status{
            PASSED,
            PASSING,
            WAITING
        }
        private String name;
        private int id;
        private int queueCount;
        private Status status;

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Person){
                if(!this.getName().equals(((Person) obj).getName())){
                    return false;
                }
                if(this.getId() != ((Person) obj).getId()){
                    return false;
                }
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return id;
        }

        void setName(String name){
            this.name = name;
        }
        void setId(int id){
            this.id = id;
        }
        void setQueueCount(int value){
            queueCount = value;
        }
        void setStatus(Status status) { this.status = status;}

        public String getName(){return this.name;}
        public int getId(){return this.id;}
        public int getQueueCount(){return this.queueCount;}
        public Status getStatus() {return this.status;}
    }

    public static void main(String[] args) throws IOException {
        String file = "src/main/botResources/database/queues/P3112";
        try {
//            createQueue("src/main/botResources/database/queues",
//                    "P3112",
//                    "src/main/botResources/database/groups/P3112.json");
            QueueEdit cq = new QueueEdit(file);
//            cq.setNextStatusByQueuePlace(cq.getFirstWaitingPersonQueuePlace());
//            cq.setNextStatusById(9);
            cq.nextStage();
            cq.nextStage();
//            cq.nextStage();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
