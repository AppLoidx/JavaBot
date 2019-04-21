package core.modules.server.messages;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * @author Arthur Kupriyanov
 */
public class Message {
    @SerializedName("message")
    private String message;
    @SerializedName("chat_id")
    private int chatId;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("id")
    private int id;

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", chatId=" + chatId +
                ", userId=" + userId +
                ", id=" + id +
                '}';
    }

    public int getId() {
        return id;
    }

    public Message setId(int id) {
        this.id = id;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Message setMessage(String message) {
        this.message = message;
        return this;
    }

    public int getChatId() {
        return chatId;
    }

    public Message setChatId(int chatId) {
        this.chatId = chatId;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Message setUserId(int userId) {
        this.userId = userId;
        return this;
    }
    public String toJSON(){
        return new GsonBuilder().create().toJson(this);
    }
}
