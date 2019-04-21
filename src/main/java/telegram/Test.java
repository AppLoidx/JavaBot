package telegram;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

/**
 * @author Arthur Kupriyanov
 */
public class Test {
    public static void main(String[] args) {
        new Thread(() -> {
            System.getProperties().put( "proxySet", "true" );
            System.getProperties().put( "socksProxyHost", "127.0.0.1" );
            System.getProperties().put( "socksProxyPort", "9150" );
            ApiContextInitializer.init();

            TelegramBotsApi botsApi = new TelegramBotsApi();

            try {
                botsApi.registerBot(new Bot());
            } catch (TelegramApiRequestException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
