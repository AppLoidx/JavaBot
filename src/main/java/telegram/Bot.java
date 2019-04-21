package telegram;

import core.Commander;
import core.commands.NaturalLanguage;
import core.modules.session.SessionInputHandler;
import core.modules.session.SessionManager;
import core.modules.vkSDK.MessageConverter;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import vk.Alias;

/**
 * @author Arthur Kupriyanov
 */
public class Bot extends TelegramLongPollingBot

{
    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage());
        Message messageT = update.getMessage();

        if (messageT.hasText()){

            com.vk.api.sdk.objects.messages.Message message = new MessageConverter().setBody(messageT.getText()).setUserId(messageT.getFrom().getId() + Integer.MIN_VALUE).buildMessage();


            if (SessionManager.checkExist(message.getUserId())){
                sendMessage(new SessionInputHandler(message.getUserId()).input(message).get(), update.getMessage().getChat().getId());
                return;
            }

            //      ALIAS

            if (Alias.isAlias(message.getUserId(), message.getBody())){
                String alias = Alias.getAlias(message.getUserId(), message.getBody());
                message = Alias.setMessageBody(message, alias);
            }

            //      NATURAL RESPONSE

            String naturalRes;
            if ((naturalRes = NaturalLanguage.getNaturalResponse(message)) != null){
                sendMessage(naturalRes, update.getMessage().getChat().getId());
                return;
            }

            // ******** MAIN HANDLE ********

            String vkResponse =  Commander.getResponse(message);
            if (vkResponse != null ) {
                if (vkResponse.equals("")) {
                    return;
                }
                sendMessage(vkResponse, update.getMessage().getChat().getId());
                return;
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "menheraitmoBot";
    }

    @Override
    public String getBotToken() {
        return "879242946:AAGN-yoex7qaDtZ_GN60QaX41Lxxu73HhuI";
    }

    private void sendMessage(String message, long chatId){
        SendMessage sm = new SendMessage()
                .setChatId(chatId)
                .setText(message);
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
