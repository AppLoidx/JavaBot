package core.commands;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.VKCommands.VKCommand;


/**
 * @author Arthur Kupriyanov
 */
public class Quest extends Command implements VKCommand, Helpable {
    @Override
    protected void setConfig() {
        commandName = "quest";
    }

    @Override
    public String exec(Message message) {
        return "Разрабатывается";
    }

    @Override
    public String getManual() {
        return "Еще разрабатывается";
    }

    @Override
    public String getDescription() {
        return "в разработке";
    }
}
