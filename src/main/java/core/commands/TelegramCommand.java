package core.commands;

import com.vk.api.sdk.objects.messages.Message;

/**
 * @author Arthur Kupriyanov
 */
public interface TelegramCommand {
    String telegramExec(Message message);
}
