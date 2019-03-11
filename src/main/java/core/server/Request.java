package core.server;

import com.google.gson.Gson;
import core.modules.notice.Notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Arthur Kupriyanov
 */
public class Request {
    private String command;
    private List<String> keys = new ArrayList<>();
    private HashMap<String, ArrayList<Object>> attachment = new HashMap<>();

    public Request addAttachment(Object o){
        ArrayList<Object> newAttachment = new ArrayList<>();
        Class clazz = o.getClass();
        if (attachment.containsKey(clazz.getSimpleName())){
            newAttachment = attachment.get(clazz.getSimpleName());
        }
        newAttachment.add(o);
        attachment.put(clazz.getSimpleName(), newAttachment);

        return this;
    }
    public Request addAttachment(Object o, String field){
        ArrayList<Object> newAttachment = new ArrayList<>();
        if (attachment.containsKey(field)){
            newAttachment = attachment.get(field);
        }
        newAttachment.add(o);
        attachment.put(field, newAttachment);

        return this;
    }

    public HashMap<String, ArrayList<Object>> getAttachment(){
        return attachment;
    }

    public Request addKey(String key) {
        keys.add(key);
        return this;
    }

    public List<String> getKeys(){
        return keys;
    }

    public String getCommand() {
        return command;
    }

    public Request setCommand(String command) {
        this.command = command;
        return this;
    }

    public static void main(String[] args) {
        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();

        Request r = new Request();
        r.setCommand("note")
                .addKey("-l")
                .addKey("-c")
                .addAttachment(new Notification("some thing",2,3,"2"));

        System.out.println(gson.toJson(r));
    }
}
