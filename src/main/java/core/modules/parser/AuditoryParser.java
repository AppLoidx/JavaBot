package core.modules.parser;

import core.modules.Time;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *  Парсер аудиторий ИТМО
 *
 * @version 1.0.0
 * @author Артур Куприянов
 */
public class AuditoryParser {

    private final String BASE_URL = "http://www.ifmo.ru/ru/schedule/2/%s/%s/schedule.htm";
    private static String[] daysName = new String[]{"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};

    private Document getDoc(String auditory) throws IOException {
        return Jsoup.connect(String.format(BASE_URL, auditory, "")).get();
    }
    private Document getDoc(String auditory, boolean parity) throws IOException {
        return Jsoup.connect(String.format(BASE_URL, auditory,parity)).get();
    }


    private Map<String, Map<String,String>> parseScheduleDoc(Document doc, int day){
        TreeMap<String,Map<String,String>> dayMap = new TreeMap<>();

        Elements schedule = doc.select("table.rasp_tabl[id=" + day + "day]");

        for (Element element: schedule.select("tr")) {
            Map<String, String> pair = new HashMap<>();

            String scheduleWeekParity = element.select("td.time").select("dt[style]").text();

            if((scheduleWeekParity.equals("")) && !element.text().equals("")) {
                pair.put("room", element.select("td.room").select("dd").text());
                pair.put("parity", element.select("td.lesson").select("dt").text().split(" ")[0]);
                pair.put("lesson", element.select("td.lesson").select("dd").text());
                pair.put("group", element.select("td.time").select("span").text().split(" ")[1]);
                dayMap.put(element.select("td.time").select("span").text().split(" ")[0], pair);
            }
        }

        return dayMap;
    }

    private String formatTime(ArrayList<String> times){
        StringBuilder sb = new StringBuilder();
        for (String time: times
             ) {
            sb.append(time);
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getFormattedSchedule(String auditory, int day, boolean parity){
        AuditoryParser ap = new AuditoryParser();
        try {
            StringBuilder sb = getLessons(ap.parseScheduleDoc(ap.getDoc(auditory, parity), day));

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка " + e.toString();
        }
    }

    public String getFormattedSchedule(String auditory, int day){
        AuditoryParser ap = new AuditoryParser();
        try {
            StringBuilder sb = getLessons(ap.parseScheduleDoc(ap.getDoc(auditory), day));

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка " + e.toString();
        }
    }

    public String getFormattedSchedule(String auditory, boolean parity){
        StringBuilder res = new StringBuilder();
        AuditoryParser ap = new AuditoryParser();
        try {
            for(int day =1; day<7;day++) {
                String sb = daysName[day - 1] +
                        "\n" +
                        getLessons(ap.parseScheduleDoc(ap.getDoc(auditory, parity), day)).toString();
                if (!sb.equals(daysName[day - 1] + "\n")){
                    res.append(sb);
                    res.append("\n");
                }

            }
            return res.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка " + e.toString();
        }
    }
    public String getFormattedSchedule(String auditory){
        StringBuilder res = new StringBuilder();
        AuditoryParser ap = new AuditoryParser();
        try {
            for(int day =1; day<7;day++) {

                String sb = daysName[day - 1] +
                        "\n" +
                        getLessons(ap.parseScheduleDoc(ap.getDoc(auditory), day)).toString();
                if (!sb.equals(daysName[day - 1] + "\n")){
                    res.append(sb);
                    res.append("\n");
                }
            }
            return res.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка " + e.toString();
        }
    }

    private StringBuilder getLessons(Map<String, Map<String, String>> scheduleDoc) {
        StringBuilder sb = new StringBuilder();
        for (String key:scheduleDoc.keySet()
        ) {
            sb.append(String.format("[%s]",key));
            sb.append("\nПара: ");
            sb.append(scheduleDoc.get(key).get("lesson"));
            sb.append("\nГруппа: ");
            sb.append(scheduleDoc.get(key).get("group"));
            sb.append("\n");
        }

        return sb;
    }

    /**
     * Возвращает форматированный список свободных промежутков времени
     * @param auditory номер аудитории
     * @param day день недели (1-7)
     * @param parity четность, <code>true</code> - четная;<br><code>false</code> - нечетная
     * @return свободные промежутки времени
     */
    public String getFormattedFreeTimes(String auditory, int day, boolean parity){
        AuditoryParser ap = new AuditoryParser();
        Time time = new Time();
        try {

            for (String key:ap.parseScheduleDoc(ap.getDoc(auditory, parity), day).keySet()
            ) {
                time.addTime(key);
            }

            return formatTime(time.getFreeTimes());

        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка " + e.toString();
        }
    }

    /**
     * Перегрузка {@link #getFormattedFreeTimes(String, int, boolean)} с добавлением фильтром длительности времени
     * @see #getFormattedFreeTimes(String, int, boolean)
     */
    public String getFormattedFreeTimes(String auditory, int day, boolean parity, int timeRange){
        AuditoryParser ap = new AuditoryParser();
        Time time = new Time();
        try {

            for (String key:ap.parseScheduleDoc(ap.getDoc(auditory, parity), day).keySet()
            ) {
                time.addTime(key);
            }

            return formatTime(time.getFreeTimes(timeRange));

        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибка " + e.toString();
        }
    }

    /**
     * @see #getFormattedFreeTimes(String, int, boolean)
     */
    public String getFormattedFreeTimes(String auditories, int day, int timeRange){
        StringBuilder response = new StringBuilder();
        for (String auditory: auditories.split("-")
             ) {
            response.append("\n-----\nАудитория: ");
            response.append(auditory);
            response.append("\n-----\n");
            AuditoryParser ap = new AuditoryParser();
            Time time = new Time();
            try {

                for (String key : ap.parseScheduleDoc(ap.getDoc(auditory), day).keySet()
                ) {
                    time.addTime(key);
                }

                response.append(formatTime(time.getFreeTimes(timeRange)));

            } catch (IOException e) {
                e.printStackTrace();
                response.append("Ошибка ").append(e.toString());
            }
        }

        return response.toString();
    }

    /**
     * @see #getFormattedFreeTimes(String, int, boolean)
     */
    public String getFormattedFreeTimes(String auditory, boolean parity, int timeRange){
        StringBuilder sb = new StringBuilder();

        for(int i = 1; i < 7; i++){
            sb.append(daysName[i-1]);
            sb.append(":\n");
            sb.append(getFormattedFreeTimes(auditory, i, parity, timeRange));
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * @see #getFormattedFreeTimes(String, int, boolean)
     */
    public String getFormattedFreeTimes(String auditory, int timeRange){

        StringBuilder sb = new StringBuilder();

        for(int i = 1; i < 7; i++){
            sb.append(daysName[i-1]);
            sb.append(":\n");
            sb.append(getFormattedFreeTimes(auditory, i, timeRange));
            sb.append("\n");
        }

        return sb.toString();
    }


    public static void main(String ... args){
        AuditoryParser ap = new AuditoryParser();

        String res = ap.getFormattedSchedule("3207");
        System.out.println(res);
    }
}
