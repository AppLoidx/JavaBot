package vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.User;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import core.Commander;

public class Messenger implements Runnable{
    private GroupActor actor;
    private VkApiClient vk;
    private String message;
    private int peerId;
    private UserXtrCounters userXtrCounters;

    public Messenger(GroupActor actor, VkApiClient vk, String message, int peerId, UserXtrCounters userXtrCounters){
        this.actor = actor;
        this.vk = vk;
        this.message = message;
        this.peerId = peerId;
        this.userXtrCounters = userXtrCounters;
    }

    private String getResponse(){
        String extra = "";
        extra += " --#user_id " + peerId;
        extra += " --#first_name " + userXtrCounters.getFirstName();
        extra += " --#last_name " + userXtrCounters.getLastName();

        return Commander.getResponse(this.message + extra);
    }

    private void sendMessage(String msg){
        if (msg == null){
            System.out.println("null");
            return;
        }
        try {
            this.vk.messages().send(this.actor).peerId(peerId).message(msg).execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        sendMessage(getResponse());
    }
}
