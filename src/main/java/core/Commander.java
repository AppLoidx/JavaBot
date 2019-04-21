package core;

import com.vk.api.sdk.objects.messages.Message;

public class Commander {

    public static String getTelegramResponse(Message message){
        return CommandDeterminant.getTelegramCommand(CommandManager.getCommands(), message).telegramExec(message);
    }
    /**
     * Обработка простых текстовых запросов.
     * @param userInput строковый запрос (может быть с метаданными)
     * @return обработанный ответ
     */
    public static String getResponse(String userInput){
        return CommandDeterminant.getCommand(CommandManager.getCommands(), userInput).init(userInput);
    }

    public static String getProgramResponse(String userInput){
        return CommandDeterminant.getProgramCommand(CommandManager.getCommands(), userInput).programInit(userInput);

    }

    /**
     * Обработка сообщений, получаемых через сервис Вконтакте. Имеет ряд дополнительной информации,
     * но не используется для обработки запросов с программ.
     * @param message сообщение (запрос) пользователя
     * @return обработанный ответ, "" - не требует отправки, null - не смог обработать
     */
    public static String getResponse(Message message){
        return CommandDeterminant.getVKCommand(CommandManager.getCommands(), message).exec(message);
    }

}
