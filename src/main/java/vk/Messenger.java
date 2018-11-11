package vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;

public class Messenger {
    private GroupActor actor;
    private VkApiClient vk;

    public Messenger(GroupActor actor, VkApiClient vk){
        this.actor = actor;
        this.vk = vk;
    }
    public void sendMessage(int peerId, String message){
        vk.messages().send(actor).message(message).peerId(peerId);
    }
}
