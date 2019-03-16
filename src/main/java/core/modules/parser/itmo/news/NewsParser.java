package core.modules.parser.itmo.news;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Arthur Kupriyanov
 */
public class NewsParser {
    private final String BASE_URL = "http://news.ifmo.ru";
    private Document getDoc() throws IOException {
        return Jsoup.connect(BASE_URL).get();
    }

    public ArrayList<News> getNews() throws IOException {

        ArrayList<News> news = new ArrayList<>();
        Elements page = getDoc().select("div.contentbox");
        news.add(parseAccent(page));
        news.addAll(parseTriple(page));

        return news;
    }

    private News parseAccent(Elements page){

        // picture
        Elements accent = page.select("div.accent");
        String imgUrl = accent.select("div.picture").select("img").attr("src");

        // News body
        Elements side = accent.select("div.side");
        String message = side.select("h3").text() + "\n" + side.select("p").text();

        // Ссылка на новость
        String url = side.select("h3").select("a").attr("href");

        return new News(message,BASE_URL + url).setImageUrl(BASE_URL + imgUrl);
    }

    private ArrayList<News> parseTriple(Elements page){
        ArrayList<News> list = new ArrayList<>();
        Elements tripleNews = page.select("ul.triplet");

        for (Element element: tripleNews.select("li")){
            String imageUrl = element.select("div.thumb").select("a").select("img").attr("src");
            String message = element.select("h4").select("a").text();
            String url = element.select("h4").select("a").attr("href");
            String date = element.select("time").text();

            list.add(new News(message, BASE_URL + url).setDate(date).setImageUrl(BASE_URL + imageUrl));
        }
        return list;
    }
}
