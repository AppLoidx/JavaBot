package core.modules.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Парсер погоды
 *
 * @version 1.0.0
 * @author Arthur Kupriyanov
 */
public class WeatherParser {
    private String city = "saint_petersburg";
    private Document doc;
    public WeatherParser() throws IOException {
        doc = Jsoup.connect(String.format("https://world-weather.ru/pogoda/russia/%s/",city)).get();
    }
    WeatherParser(String city) throws IOException {
        this.city = city;
        doc = Jsoup.connect(String.format("https://world-weather.ru/pogoda/russia/%s/",city)).get();
    }

    public String getWeatherFullDescription() throws IOException {
        Elements elements = doc.select("span.dw-into");
        return elements.text().replace("Подробнее", "").replace("Скрыть","");
    }

    public String getWeatherTodayDescription() throws IOException {
        Elements elements = doc.select("span.dw-into");
        return elements.text().split("Подробнее")[0];
    }

    public String getTemperature(int day){
        return getDaysTemperature().get(day);
    }

    private ArrayList<String> getDaysTemperature(){
        Elements elements = doc.select("div.day-temperature");
        return new ArrayList<>(elements.eachText());
    }

    public static void main(String ... args){
        try {
            System.out.println(new WeatherParser().getDaysTemperature());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
