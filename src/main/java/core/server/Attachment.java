package core.server;

import com.google.gson.Gson;
import core.modules.notice.Notification;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Arthur Kupriyanov
 */
public class Attachment {

    private HashMap<String, ArrayList<Object>> attachment = new HashMap<>();
    public Attachment addAttachment(Object o){
        ArrayList<Object> newAttachment = new ArrayList<>();
        Class clazz = o.getClass();
        if (attachment.containsKey(clazz.getSimpleName())){
            newAttachment = attachment.get(clazz.getSimpleName());
        }
        newAttachment.add(o);
        attachment.put(clazz.getSimpleName(), newAttachment);

        return this;
    }


    public static void main(String[] args) {
        Attachment a = new Attachment();
        Notification note = new Notification("45",1,1,"21");
        a.addAttachment(new Notification("12",1,1,"23"));
        Gson gson = new Gson().newBuilder().create();

        String json = gson.toJson(a);
        System.out.println(json);
        Attachment o = gson.fromJson(json, Attachment.class);

        System.out.println(((Notification) o.attachment.get("Notification").get(0)).message);
    }
}
