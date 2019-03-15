package core.modules.vkSDK;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.vk.api.sdk.objects.base.BoolInt;
import com.vk.api.sdk.objects.base.Geo;
import com.vk.api.sdk.objects.messages.Action;
import com.vk.api.sdk.objects.messages.ChatPushSettings;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAttachment;

import java.util.List;

/**
 * @author Arthur Kupriyanov
 */
public class MessageConverter {

    /**
     * Message ID
     */
    @SerializedName("id")
    private Integer id;
    /**
     * Date when the message has been sent in Unixtime
     */
    @SerializedName("date")
    private Integer date;
    /**
     * Date when the message has been updated in Unixtime
     */
    @SerializedName("update_time")
    private Integer updateTime;
    /**
     * Information whether the message is outcoming
     */
    @SerializedName("out")
    private BoolInt out;
    /**
     * Message author's ID
     */
    @SerializedName("user_id")
    private Integer userId;
    /**
     * Message author's ID
     */
    @SerializedName("from_id")
    private Integer fromId;
    /**
     * ID used for sending messages. It returned only for outgoing messages
     */
    @SerializedName("random_id")
    private Integer randomId;
    /**
     * Is it an important message
     */
    @SerializedName("important")
    private BoolInt important;
    /**
     * Is it an deleted message
     */
    @SerializedName("deleted")
    private BoolInt deleted;
    /**
     * Whether the message contains smiles
     */
    @SerializedName("emoji")
    private BoolInt emoji;
    /**
     * Forwarded messages
     */
    @SerializedName("fwd_messages")
    private List<Message> fwdMessages;
    /**
     * Information whether the messages is read
     */
    @SerializedName("read_state")
    private BoolInt readState;
    /**
     * Message title or chat title
     */
    @SerializedName("title")
    private String title;
    /**
     * Message text
     */
    @SerializedName("body")
    private String body;
    @SerializedName("attachments")
    private List<MessageAttachment> attachments;
    /**
     * Chat ID
     */
    @SerializedName("chat_id")
    private Integer chatId;
    @SerializedName("chat_active")
    private List<Integer> chatActive;
    /**
     * Push settings for the chat
     */
    @SerializedName("push_settings")
    private ChatPushSettings pushSettings;
    /**
     * Action type
     */
    @SerializedName("action")
    private Action action;
    /**
     * User or email ID has been invited to the chat or kicked from the chat
     */
    @SerializedName("action_mid")
    private Integer actionMid;
    /**
     * Email has been invited or kicked
     */
    @SerializedName("action_email")
    private String actionEmail;
    /**
     * Action text
     */
    @SerializedName("action_text")
    private String actionText;
    /**
     * Chat users number
     */
    @SerializedName("users_count")
    private Integer usersCount;
    /**
     * Chat administrator ID
     */
    @SerializedName("admin_id")
    private Integer adminId;
    /**
     * URL of the preview image with 50px in width
     */
    @SerializedName("photo_50")
    private String photo50;
    /**
     * URL of the preview image with 100px in width
     */
    @SerializedName("photo_100")
    private String photo100;
    /**
     * URL of the preview image with 200px in width
     */
    @SerializedName("photo_200")
    private String photo200;
    @SerializedName("geo")
    private Geo geo;

    public Message buildMessage(){
        Gson gson = new Gson().newBuilder().create();
        String json = gson.toJson(this);

        return gson.fromJson(json, Message.class);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public void setOut(BoolInt out) {
        this.out = out;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public void setRandomId(Integer randomId) {
        this.randomId = randomId;
    }

    public void setImportant(BoolInt important) {
        this.important = important;
    }

    public void setDeleted(BoolInt deleted) {
        this.deleted = deleted;
    }

    public void setEmoji(BoolInt emoji) {
        this.emoji = emoji;
    }

    public void setFwdMessages(List<Message> fwdMessages) {
        this.fwdMessages = fwdMessages;
    }

    public void setReadState(BoolInt readState) {
        this.readState = readState;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setAttachments(List<MessageAttachment> attachments) {
        this.attachments = attachments;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public void setChatActive(List<Integer> chatActive) {
        this.chatActive = chatActive;
    }

    public void setPushSettings(ChatPushSettings pushSettings) {
        this.pushSettings = pushSettings;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setActionMid(Integer actionMid) {
        this.actionMid = actionMid;
    }

    public void setActionEmail(String actionEmail) {
        this.actionEmail = actionEmail;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public void setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public void setPhoto50(String photo50) {
        this.photo50 = photo50;
    }

    public void setPhoto100(String photo100) {
        this.photo100 = photo100;
    }

    public void setPhoto200(String photo200) {
        this.photo200 = photo200;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("id=").append(id);
        sb.append(", date=").append(date);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", out=").append(out);
        sb.append(", userId=").append(userId);
        sb.append(", fromId=").append(fromId);
        sb.append(", randomId=").append(randomId);
        sb.append(", important=").append(important);
        sb.append(", deleted=").append(deleted);
        sb.append(", emoji=").append(emoji);
        sb.append(", fwdMessages=").append(fwdMessages);
        sb.append(", readState=").append(readState);
        sb.append(", title='").append(title).append('\'');
        sb.append(", body='").append(body).append('\'');
        sb.append(", attachments=").append(attachments);
        sb.append(", chatId=").append(chatId);
        sb.append(", chatActive=").append(chatActive);
        sb.append(", pushSettings=").append(pushSettings);
        sb.append(", action=").append(action);
        sb.append(", actionMid=").append(actionMid);
        sb.append(", actionEmail='").append(actionEmail).append('\'');
        sb.append(", actionText='").append(actionText).append('\'');
        sb.append(", usersCount=").append(usersCount);
        sb.append(", adminId=").append(adminId);
        sb.append(", photo50='").append(photo50).append('\'');
        sb.append(", photo100='").append(photo100).append('\'');
        sb.append(", photo200='").append(photo200).append('\'');
        sb.append(", geo=").append(geo);
        sb.append('}');
        return sb.toString();
    }
}
