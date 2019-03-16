package core.commands;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import core.commands.VKCommands.VKCommand;
import core.modules.parser.itmo.news.NewsParser;
import vk.VKManager;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Arthur Kupriyanov
 */
public class News extends Command implements VKCommand, Helpable {
    @Override
    protected void setConfig() {
        commandName = "news";
    }

    @Override
    public String exec(Message message) {

        try {
            ArrayList<core.modules.parser.itmo.news.News> news = new NewsParser().getNews();

            news.stream().limit(4).forEach((n) -> {

                System.out.println(n);
                MessagesSendQuery query = new VKManager().getSendQuery();

                String msg = n.getMessage();
                if (n.getDate() != null){
                    msg += "\nДата: " + n.getDate();
                }

                query.attachment(n.getUrl());
                query.peerId(message.getUserId());
                if (n.getImageUrl() != null){
                    msg += "\n" + n.getUrl();
                }

                query.message(msg);

                try {
                    query.execute();
                } catch (ApiException | ClientException e) {
                    e.printStackTrace();
                }


            });

            return "";

        } catch (IOException e) {
            return "Не удалось получить новости";
        }
    }

    @Override
    public String getManual() {
        return "На данный момент ключей нету\n" +
                "Чтобы посомотреть новости введите: news\n" +
                "Программа покажет последние 4 новости из сайта news.ifmo.ru" +
                " последние 4 новости. (Бета-версия команды)";
    }

    @Override
    public String getDescription() {
        return "Показывает 4 последние новости (бета)";
    }
}
