package core.modules.parser;

import core.modules.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Парсит расписание групп ИТМО из <a href=www.ifmo.ru>ifmo</a>
 *
 * @author Артур Куприянов
 * @version 1.2.0
 */
public class ScheduleParser {

    private static final String BASE_URL = "http://www.ifmo.ru/ru/schedule/0/%s/raspisanie_zanyatiy_%s.htm";
    private static String[] daysName = new String[]{"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};

    /**
     * Используется сторонняя библиотека {@link org.jsoup.Jsoup}<br>
     * Возвращает расписание в виде словаря с ключом времени и значением вложенным словарем со значениями
     * для ондого дня<br>
     *
     * room -- комната занятий<br>
     * place -- место (адрес)<br>
     * lesson -- название пары
     *
     * @param day значение от 1-7 (день недели)
     * @param group название группы
     * @param evenWeek {@code true} - четная неделя, {@code false} - нечетная
     * @return Расписание одного дня (сортированный по времени)
     * @throws IOException использование методов {@link org.jsoup.Jsoup}
     */
    private Map<String,Map<String,String>> getDaySchedule(int day, String group, boolean evenWeek) throws IOException {

        Document doc = Jsoup.connect(String.format(BASE_URL,group,group)).get();
        return parseScheduleDoc(doc, evenWeek, day);
    }

    private Map<String,Map<String,String>> parseScheduleDoc(Document doc, boolean evenWeek, int day){
        TreeMap<String,Map<String,String>> dayMap = new TreeMap<>();
        Elements schedule = doc.select("table.rasp_tabl[id="+ day + "day]");
        String weekParity = "нечетная неделя";
        if (evenWeek){
            weekParity = "четная неделя";
        }
        for (Element element: schedule.select("tr")) {
            Map<String, String> pair = new HashMap<>();

            String scheduleWeekParity = element.select("td.time").select("dt[style]").text();


            if((scheduleWeekParity.equals(weekParity) || scheduleWeekParity.equals("")) && !element.text().equals("")) {
                parsePairs(element, pair);
                dayMap.put(element.select("td.time").select("span").text().split(" ")[0], pair);
            }
        }
        return dayMap;
    }

    private Map<String,Map<String,String>> parseScheduleDoc(Document doc, int day){
        TreeMap<String,Map<String,String>> dayMap = new TreeMap<>();

        Elements schedule = doc.select("table.rasp_tabl[id=" + day + "day]");

        for (Element element: schedule.select("tr")) {
            Map<String, String> pair = new HashMap<>();

            String scheduleWeekParity = element.select("td.time").select("dt[style]").text();
            String time = element.select("td.time").select("span").text().split(" ")[0];
            if (!(time.equals(""))){
                parsePairs(element, pair);
                dayMap.put(String.format("%s %s",
                        time ,scheduleWeekParity),
                        pair);
        }
        }
        return dayMap;
    }

    private void parsePairs(Element element, Map<String, String> pair) {
        pair.put("place", element.select("td.room").select("span").text());
        pair.put("room", element.select("td.room").select("dd").text());
        pair.put("lesson", element.select("td.lesson").select("yobject").text() +
                element.select("td.lesson").select("dd").text());
    }

    /**
     * Возвращает форматированное расписание, используя {@link #getDaySchedule(int, String, boolean)}
     * @param day значение от 1-7 (день недели)
     * @param group название группы
     * @param evenWeek {@code true} - четная неделя, {@code false} - нечетная
     * @return форматированное расписание
     * @throws IOException при вызове функции {@link #getDaySchedule(int, String, boolean)}
     */
    public String formattedDaySchedule(int day, String group, boolean evenWeek) throws IOException {
        return scheduleFormat(getDaySchedule(day, group, evenWeek));
    }

    private String scheduleFormat(Map<String,Map<String,String>> daySchedule){
        final String[] response = {""};
        daySchedule.forEach((time, pair) ->
                response[0] += String.format("[%s] \nПара: %s\nАуд: %s\nМесто: %s\n%s\n",
                        time, pair.get("lesson"), pair.get("room"), pair.get("place"), "-----------------"));

        return response[0];
    }

    /**
     * Возврашает расписание всей недели с указанной четностью
     * @param group название группы
     * @param evenWeek <code>true</code> - четная; <br><code>false</code> - нечетная
     * @return Отформатированное расписание всей недели с указанной четностью
     * @throws IOException вызов методов {@link Jsoup}
     */
    public String formattedAllSchedule(String group, boolean evenWeek) throws IOException {

        StringBuilder response = new StringBuilder();

        Document doc = Jsoup.connect(String.format(BASE_URL,group,group)).get();
        for (int day: new int[]{1,2,3,4,5,6}
             ) {
            Map<String,Map<String,String>> dayMap = parseScheduleDoc(doc, evenWeek, day);
            response.append(daysName[day-1]).append("\n\n");
            response.append(scheduleFormat(dayMap)).append("\n");
        }
        return response.toString();
    }

    /**
     * Перегруженный метод {@link #formattedAllSchedule(String, boolean)}
     * Без указания четности. Выводит все расписание, не учитывая четность.
     * @param group Название группы
     * @return Отформатированное расписание всей недели
     * @throws IOException вызов методов {@link Jsoup}
     */
    public String formattedAllSchedule(String group) throws IOException {

        StringBuilder response = new StringBuilder();

        Document doc = Jsoup.connect(String.format(BASE_URL,group,group)).get();
        for (int day: new int[]{1,2,3,4,5,6}
             ) {
            Map<String,Map<String,String>> dayMap = parseScheduleDoc(doc, day);
            response.append(daysName[day-1]).append("\n\n");
            response.append(scheduleFormat(dayMap)).append("\n");
        }
        return response.toString();
    }


    /**
     * Получает четность недели путем сводки текущей недели года и четности в ifmo
     * @return <code>true</code> - четная неделя;<br> <code>false</code> - нечетная неделя
     */
    public static boolean getWeekParity(){

        boolean weekEven = Date.getWeekOfYear()%2 == 0 ? false : true;
        return weekEven;
    }

    public static boolean getWeekParity(int increase){
        boolean weekEven = getWeekParity();
        int day = Date.getDayOfWeek();
        if ((((day + increase - 1)/ 7) % 2 != 0)){
            weekEven = !weekEven;
        }

        return weekEven;
    }


}
