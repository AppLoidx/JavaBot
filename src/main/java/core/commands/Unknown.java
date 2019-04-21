package core.commands;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.VKCommands.VKCommand;

public class Unknown extends Command implements ProgramSpecification, VKCommand, TelegramCommand {

    @Override
    protected void setConfig() {
        commandName = "unknown";
    }
    @Override
    public String init(String... args) {
        return "У меня нет распознавателя простой речи.\n" +
                "Не понимаю эту команду...";
    }

    @Override
    public String programInit(String... args) {
        return null;
    }

    @Override
    public String exec(Message message) {
        return null;
    }

    @Override
    public String telegramExec(Message message) {
        return exec(message);
    }
}
