package core.commands;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAttachment;
import com.vk.api.sdk.objects.messages.MessageAttachmentType;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import core.commands.VKCommands.VKCommand;
import core.common.KeysReader;
import core.modules.UsersDB;
import vk.VKManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Команда рассылки сообщений
 * @author Arthur Kupriyanov
 */
public class Spam extends Command implements VKCommand {
    @Override
    protected void setConfig() {
        commandName = "spam";
    }

    @Override
    public String exec(Message message) {
        if (message.getUserId() != 255396611) return "";
        Map<String, String> keyMap = KeysReader.readKeys(message.getBody().split(" "));
        String group;
        if (keyMap.containsKey("-g")){
            group = keyMap.get("-g");
        } else {
            return "укажите группу через ключ -g";
        }

        String msg;
        if (keyMap.containsKey("-m")){
            msg = keyMap.get("-m");
        } else {
            return "Укажите сообщение через ключ -m";
        }

        UserXtrCounters user = VKManager.getUserInfo(message.getUserId());
        if (user != null) {
            if (!keyMap.containsKey("-a") || message.getUserId()!=255396611)
            msg += "\nСообщение от: " + user.getFirstName() + " " + user.getLastName();
        }

        UsersDB usersDB = new UsersDB();
        try {
            Map<Integer, String> users = usersDB.getVKIDListWithGroup();
            for (int vkid : users.keySet()){
                if (group.equals("*")) group =".*";
                if (users.get(vkid).equals(group) || users.get(vkid).matches(group)){
                    sendMessage(vkid, msg, message);
                }
            }
            return "Хорошо!";

        } catch (SQLException e) {
            return "Ошибка " + e.getMessage();
        }

    }

    private void sendMessage(int vkid, String msg, Message vkMessage){
        MessagesSendQuery query = new VKManager().getSendQuery().peerId(vkid).message(msg);

        List<MessageAttachment> attachments = vkMessage.getAttachments();
        if (attachments != null) {
            for (MessageAttachment a : attachments) {
                MessageAttachmentType attach = a.getType();
                if (attach == MessageAttachmentType.PHOTO) {
                    query.attachment(attach.getValue() + a.getPhoto().getOwnerId() + "_" + a.getPhoto().getId() + "_" + a.getPhoto().getAccessKey());
                }
                if (attach == MessageAttachmentType.DOC) {
                    query.attachment(a.getDoc().getUrl());
                }
            }
        }

        try {
            query.execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }
}
