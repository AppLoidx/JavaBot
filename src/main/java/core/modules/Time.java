package core.modules;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Класс для работы с промежутками времени
 * В основном, для выделения свободных промежутков времени, или
 * наоборот занятых
 *
 * @version 1.0.0
 * @author Артур Куприянов
 */
public class Time {

    protected int defaultStartTime = 500;   // 08:20
    protected int defaultEndTime = 1380;    // 21:00
    protected String defaultHMRegex = ":";  // разделитель часов и минут
    protected String defaultTimeRegex = "-";    // разделитель между двумя временами

    private TreeMap<Type, TreeMap<Integer, Integer>> timeMap = new TreeMap<>();

    private int startTime;
    private int endTime;
    private String hmRegex;
    private String timeRegex;


    enum Type{
        OCCUPIED,
        FREE
    }

    public Time(){
        startTime = defaultStartTime;
        endTime = defaultEndTime;
        hmRegex = defaultHMRegex;
        timeRegex = defaultTimeRegex;

        addTimesToMap();
    }

    public Time(int startTime, int endTime){
        this.startTime = startTime;
        this.endTime = endTime;
        hmRegex = defaultHMRegex;
        timeRegex = defaultTimeRegex;

        addTimesToMap();
    }

    public Time(String hmRegex, String timeRegex){
        this.hmRegex = hmRegex;
        this.timeRegex = timeRegex;

        addTimesToMap();
    }

    public Time(int startTime, int endTime,String hmRegex, String timeRegex){
        this.startTime = startTime;
        this.endTime = endTime;
        this.hmRegex = hmRegex;
        this.timeRegex = timeRegex;

        addTimesToMap();
    }

    /**
     * Добавляет оккупированное время во временной промежуток
     * Также вызывает {@link #updateTimeMap()}
     *
     * @param startTime начало временного промежутка
     * @param endTime конец временного промежутка
     */
    public void addTime(int startTime, int endTime){
        addToTimeMap(Type.OCCUPIED, startTime, endTime);
        updateTimeMap();
    }

    /**
     * Перегрузка {@link #addTime(int, int)}
     * @param formattedString форматированный временной промежуток, пример по умолчанию: <b>09:00-10:00</b>
     */
    public void addTime(String formattedString){
        String[] times = formattedString.split(timeRegex);
        startTime = Integer.valueOf(times[0].split(hmRegex)[0]) * 60 + Integer.valueOf(times[0].split(hmRegex)[1]);
        endTime = Integer.valueOf(times[1].split(hmRegex)[0]) * 60 + Integer.valueOf(times[1].split(hmRegex)[1]);
        addTime(startTime, endTime);
    }

    public TreeMap<Type, TreeMap<Integer, Integer>> getTimeMap(){
        return timeMap;
    }

    public ArrayList<String> getFreeTimes(int timeRange){
        String formattedTime;
        ArrayList<String> response = new ArrayList<>();
        for (int key: timeMap.get(Type.FREE).keySet()
             ) {
            formattedTime = formattedTime(key) + timeRegex + formattedTime(timeMap.get(Type.FREE).get(key));
            if (deltaTime(formattedTime) >= timeRange) {
                response.add(formattedTime);
            }
        }

        return response;
    }

    public ArrayList<String> getFreeTimes(){
        return getFreeTimes(0);
    }

    private void addToTimeMap(Type type, int key, int value){
        TreeMap<Integer, Integer> timeMapValue = timeMap.get(type);
        if (timeMapValue.containsKey(key)){
            timeMapValue.replace(key, value);
        }else {
            timeMapValue.put(key, value);
        }

        timeMap.put(type, timeMapValue);
    }

    private void updateTimeMap(){

        for (int occupiedTime: timeMap.get(Type.OCCUPIED).keySet()
             ) {

            for (int freeTime : timeMap.get(Type.FREE).keySet()) {

                if (occupiedTime>= freeTime ){
                    int tempEndFreeTime = timeMap.get(Type.FREE).get(freeTime);
                    int tempEndOccupiedTime = timeMap.get(Type.OCCUPIED).get(occupiedTime);
                    if (tempEndOccupiedTime <= tempEndFreeTime) {
                        addToTimeMap(Type.FREE, freeTime, occupiedTime);


                        if (tempEndFreeTime != tempEndOccupiedTime) {
                            addToTimeMap(Type.FREE, tempEndOccupiedTime, tempEndFreeTime);
                        }

                        break;
                    }
                }
            }
        }

        // Избежание ошибки ConcurrentModificationException, очистка значений, где ключ = значение
        while(cleanFreeTimeMap());
    }

    private boolean cleanFreeTimeMap(){
        boolean deleted = false;
        for (int key: timeMap.get(Type.FREE).keySet()
        ) {
            if (key == timeMap.get(Type.FREE).get(key)){
                removeValueFromTimeMap(Type.FREE, key);
                deleted = true;
                break;
            }
        }
        return deleted;
    }

    private void removeValueFromTimeMap(Type type, int key){
        TreeMap<Integer, Integer> oldTimeMap = timeMap.get(type);

        for (int mapKey: oldTimeMap.keySet()
             ) {
            if (mapKey == key){
                oldTimeMap.remove(key);
                break;
            }
        }
        timeMap.put(type,oldTimeMap);
    }
    private void addTimesToMap(){
        TreeMap<Integer, Integer> freeTimeMap = new TreeMap<>();
        TreeMap<Integer, Integer> occupiedTimeMap = new TreeMap<>();
        freeTimeMap.put(startTime, endTime);
        timeMap.put(Type.FREE, freeTimeMap);
        timeMap.put(Type.OCCUPIED, occupiedTimeMap);
    }

    private String formattedTime(int time) {
        String hour = String.valueOf((int) time / 60);
        String minute = String.valueOf((int) time % 60);

        if (hour.length() < 2) {
            hour = "0" + hour;
        }
        if (minute.length() < 2) {
            minute = "0" + minute;
        }

        return hour + hmRegex + minute;
    }

    private int parseFormattedTime(String formattedTime){
        return Integer.valueOf(formattedTime.split(hmRegex)[0]) * 60 +
                Integer.valueOf(formattedTime.split(hmRegex)[1]);
    }

    /**
     * Возвращает длительность промежутка времени
     * @param rangeTime форматированное время типа XX:XX-XX:XX
     * @return длину промежутка в минутах
     */
    public int deltaTime(String rangeTime){
        String time1 =  rangeTime.split(timeRegex)[0];
        String time2 =  rangeTime.split(timeRegex)[1];
        return parseFormattedTime(time2) - parseFormattedTime(time1);
    }


}
