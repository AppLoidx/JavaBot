package server;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Arthur Kupriyanov
 */
public class Response {
    private int status;
    private String message;

    private HashMap<String, ArrayList<Object>> attachment = new HashMap<>();

    public void addAttachment(Object o){
        ArrayList<Object> newAttachment = new ArrayList<>();
        Class clazz = o.getClass();
        if (attachment.containsKey(clazz.getSimpleName())){
            newAttachment = attachment.get(clazz.getSimpleName());
        }
        newAttachment.add(o);
        attachment.put(clazz.getSimpleName(), newAttachment);
    }

    public HashMap<String, ArrayList<Object>> getAttachment(){
        return attachment;
    }
    public Response setStatus(int status){
        this.status = status;
        return this;
    }

    public Response setMessage(String message){
        this.message = message;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
