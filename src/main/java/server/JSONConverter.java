package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.modules.notice.Notification;
import core.modules.queue.Person;
import core.modules.queue.SimpleQueue;

import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Arthur Kupriyanov
 */
public class JSONConverter {
    private static final Gson gson = new GsonBuilder().create();

    public static String convertToJson(Object o){
        return gson.toJson(o);
    }
    public static Response getResponse(String json){
        return gson.fromJson(json, Response.class);
    }
    public static Request getRequest(String json){
        return gson.fromJson(json, Request.class);
    }

    public static void main(String[] args) {
        Request request = new Request().addKey("-k").setCommand("lol");

        request.addAttachment(new Notification("msg", 2, 3,"12"));
        SimpleQueue sq = new SimpleQueue("first_queue");
        sq.addPerson(new Person("Malk Balish", 146060));
        sq.addPerson(new Person("London Horizon", 100023));
        sq.addPerson(new Person("Hayazawa",100123));

        request.addAttachment(QueueObject.convertQueueToJSON(sq), "queue");

        String req = JSONConverter.convertToJson(request);

        System.out.println(req);

        Request n2 = JSONConverter.getRequest(req);

        QueueObject queue = gson.fromJson(n2.getAttachment().get("queue").get(0).toString(), QueueObject.class);

        System.out.println(queue.getType());

        Map<Integer, Person> queueInner = queue.getQueue();

        Stream stream = queueInner.values().stream();
        stream.forEach((Object p) -> {
            System.out.println(((Person) p).getName());
        });

        SimpleQueue sqq = QueueBuilder.createSimpleQueue(queue);

        System.out.println(sqq.getUsers());
        System.out.println(sqq.getFormattedQueue());
    }
}
