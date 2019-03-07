package core.modules.parser;

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
 * @author Arthur Kupriyanov
 */
public class TeacherScheduleParser {
    private static final String BASE_URL = "http://www.ifmo.ru/ru/schedule/3/%s/%s/raspisanie_zanyatiy.htm";
    private static String[] daysName = new String[]{"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};

    private Document getDoc(int teacherID) throws IOException {
        return Jsoup.connect(String.format(BASE_URL, teacherID, "")).get();
    }
    private Document getDoc(int teacherID, boolean parity) throws IOException {
        String parityString = parity == true ? "1" : "2";
        return Jsoup.connect(String.format(BASE_URL, String.valueOf(teacherID),parityString)).get();
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
        Elements schedule = doc.select("table.rasp_tabl[id="+ day + "day]");
//        String weekParity = "нечетная неделя";
//        if (evenWeek){
//            weekParity = "четная неделя";
//        }
        for (Element element: schedule.select("tr")) {
            Map<String, String> pair = new HashMap<>();

            String scheduleWeekParity = element.select("td.time").select("dt[style]").text();


            if(!element.text().equals("")) {
                parsePairs(element, pair);
                dayMap.put(element.select("td.time").select("span").text().split(" ")[0], pair);
            }
        }
        return dayMap;
    }
    private ArrayList<Map<String,Map<String,String>>> parseScheduleDoc(Document doc, boolean evenWeek){
        ArrayList<Map<String,Map<String,String>>> pairs = new ArrayList<>();
        for(int day : new int[]{1,2,3,4,5,6}) {
            pairs.add(parseScheduleDoc(doc, evenWeek, day));
        }
        return pairs;
    }
    private ArrayList<Map<String,Map<String,String>>> parseScheduleDoc(Document doc){
        ArrayList<Map<String,Map<String,String>>> pairs = new ArrayList<>();
        for(int day : new int[]{1,2,3,4,5,6}) {
            pairs.add(parseScheduleDoc(doc, day));
        }
        return pairs;
    }

    private void parsePairs(Element element, Map<String, String> pair) {
        pair.put("place", element.select("td.room").select("span").text());
        pair.put("room", element.select("td.room").select("dd").text());
        String dailyPair = element.select("td").select("span").text().split(" ")[3];
        if (!dailyPair.matches("[a-zA-Z][0-9].*")){
            dailyPair = element.select("td").select("span").text().split(" ")[1];
        }
        pair.put("group", dailyPair);
        pair.put("lesson", element.select("td.lesson").select("yobject").text() +
                element.select("td.lesson").select("dd").text());
        pair.put("even", element.select("td.time").select("dt[style]").text());
    }

    private String getLessons(Map<String, Map<String, String>> scheduleDoc) {
        StringBuilder sb = new StringBuilder();
        for (String key:scheduleDoc.keySet()
        ) {
            sb.append(String.format("[%s]",key));
            sb.append("\nПара: ");
            sb.append(scheduleDoc.get(key).get("lesson"));
            sb.append("\nГруппа: ");
            sb.append(scheduleDoc.get(key).get("group"));
            sb.append("\nМесто: ");
            sb.append(scheduleDoc.get(key).get("place"));
            sb.append("\n");
            sb.append("\nЧетность: ");
            sb.append(scheduleDoc.get(key).get("even"));
        }

        return sb.toString();
    }

    public String getFormattedSchedule(int teacherID) throws IOException {
        ArrayList<Map<String, Map<String, String>>> parsedDocPairs = parseScheduleDoc(getDoc(teacherID));
        StringBuilder sb = new StringBuilder();
        int day = 0;
        for(Map<String, Map<String, String>> parsedInnerDoc : parsedDocPairs){
            String schedule;
            if (!(schedule = getLessons(parsedInnerDoc)).equals("")) {
                sb.append("\n").append(daysName[day++]).append(":\n");
                sb.append("\n");
                sb.append(getLessons(parsedInnerDoc));
                sb.append("\n---------------\n");
            }
        }

        return sb.toString();
    }


    public String getFormattedSchedule(int teacherID, int day, boolean evenWeek) throws IOException {
        Map<String, Map<String, String>> parsedDoc = parseScheduleDoc(getDoc(teacherID), evenWeek, day);
        return getLessons(parsedDoc);
    }
    public String getFormattedSchedule(int teacherID, int day) throws IOException {
        Map<String, Map<String, String>> parsedDoc = parseScheduleDoc(getDoc(teacherID), day);
        return getLessons(parsedDoc);
    }

    public String getFormattedSchedule(int teacherID, boolean evenWeek) throws IOException {
        ArrayList<Map<String, Map<String, String>>> parsedDocPairs = parseScheduleDoc(getDoc(teacherID), evenWeek);
        StringBuilder sb = new StringBuilder();
        int day = 0;
        for(Map<String, Map<String, String>> parsedInnerDoc : parsedDocPairs){
            String schedule;
            if (!(schedule = getLessons(parsedInnerDoc)).equals("")) {
                sb.append("\n").append(daysName[day++]).append(":\n");
                sb.append("\n");
                sb.append(getLessons(parsedInnerDoc));
                sb.append("\n---------------\n");
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        TeacherScheduleParser tsp = new TeacherScheduleParser();
//        System.out.println(
//        tsp.parseScheduleDoc(tsp.getDoc(146060), true, 1));
//        System.out.println(
//        tsp.parseScheduleDoc(tsp.getDoc(146060), true, 2));
//        System.out.println(
//        tsp.parseScheduleDoc(tsp.getDoc(146060), true, 3));
//        System.out.println(
//        tsp.parseScheduleDoc(tsp.getDoc(146060), true, 4));
        System.out.println(
        tsp.parseScheduleDoc(tsp.getDoc(146060), true, 5));
        System.out.println(tsp.getFormattedSchedule(146060, true));
        System.out.println(tsp.getFormattedSchedule(140060, 1));
        System.out.println(tsp.getFormattedSchedule(140060));
    }
}
