package core.commands.VKCommands;

import com.vk.api.sdk.objects.messages.Message;

/**
 * @author Arthur Kupriyanov
 */
public interface VKCommand {
    public String exec(Message message);
}
