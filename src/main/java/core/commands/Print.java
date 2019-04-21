package core.commands;

import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAttachment;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import core.commands.VKCommands.VKCommand;
import vk.VKManager;

/**
 * @author Arthur Kupriyanov
 */
public class Print extends Command implements VKCommand {
    @Override
    protected void setConfig() {
        commandName = "print";
    }

    @Override
    public String exec(Message message) {
        MessagesSendQuery query = new VKManager().getSendQuery();
        for (MessageAttachment attach : message.getAttachments()){
            //query.attachment(attach)
        }
        return null;
    }
}
