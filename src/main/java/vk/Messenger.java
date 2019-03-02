package vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import core.Commander;

public class Messenger implements Runnable{
    private GroupActor actor;
    private VkApiClient vk;
    private Message message;
    private int peerId;

    public Messenger(GroupActor actor, VkApiClient vk, Message message){
        this.actor = actor;
        this.vk = vk;
        this.message = message;
        this.peerId = message.getUserId();
    }

    private String getResponse(){

        String vkResponse =  Commander.getResponse(message);
        if (vkResponse != null){
            return vkResponse;
        } else {
            String extra = "";
            extra += " --#user_id " + message.getUserId();
            UserXtrCounters info = getUserInfo(message.getId());
            extra += " --#first_name " + info.getFirstName();
            extra += " --#last_name " + info.getLastName();
            return Commander.getResponse(message.getBody() + extra);
        }
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
