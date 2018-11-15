package vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import core.Commander;

public class Messenger implements Runnable{
    private GroupActor actor;
    private VkApiClient vk;
    private String message;
    private int peerId;

    public Messenger(GroupActor actor, VkApiClient vk, String message, int peerId){
        this.actor = actor;
        this.vk = vk;
        this.message = message;
        this.peerId = peerId;
    }

    private String getResponse(){
        return Commander.getResponse(this.message);
    }

    private void sendMessage(String msg){
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
