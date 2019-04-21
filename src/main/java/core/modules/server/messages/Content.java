package core.modules.server.messages;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Arthur Kupriyanov
 */
public class Content {
    @SerializedName("content")
    private TreeMap<Integer, Message> content;
    @SerializedName("chat_id")
    private int chatId;
    @SerializedName("max_msg_id")
    private int maxMsgId = -1;

    public Content(TreeMap<Integer,Message> content, int chatId) {
        this.content = content;
        this.chatId = chatId;
    }

    public Content(int chatId) {
        this.chatId = chatId;
        this.content = new TreeMap<>();
    }

    @Override
    public String toString() {
        return "Content{" +
                "content=" + content +
                ", chatId=" + chatId +
                '}';
    }

    public Map<Integer,Message> getContent() {
        return content;
    }

    public void setContent(Map<Integer, Message> content) {
        this.content.putAll(content);
    }

    public void addMessage(Message msg){
        msg.setId(++maxMsgId);
        this.content.put(msg.getId(), msg);
    }

    public int getMaxMsgId() {
        return maxMsgId;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String toJSON(){
        return new GsonBuilder().create().toJson(this);
    }
}
