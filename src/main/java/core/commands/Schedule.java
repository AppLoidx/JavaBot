package core.commands;

import core.common.KeysReader;
import core.modules.Date;
import core.modules.parser.AuditoryParser;
import core.modules.parser.ScheduleParser;

import java.io.IOException;
import java.util.Map;

/**
 * @author Артур Куприянов
 * @version 1.3
 */
public class Schedule extends Command{


    @Override
    protected void setName() {
        name = "schedule";
    }

    private ScheduleParser p = new ScheduleParser();

    /**
     * Ключи: <br>
     *     -f {value} - минимальная длительность свободного времени <br>
     *     -p - четность [1 - четная, 2 - нечетная] <br>
     *     -g {value} - группа <br>
     *     -d {value} - количество дней на перед <br>
     *     -r {value} - номер аудитории <br>
     *     -l - используется с ключом -r, показывает расписание лекций <br>
     * @param args ввод пользователя разделенный на массив через пробел <br>
     * @return Ответ пользователю
     */
    @Override
    public String init(String... args) {

        int dayOfWeek = Date.getDayOfWeek();

        // DEFAULT VALUES
        String group = "P3112";     //TODO: Get from user settings
        int day = dayOfWeek;
        boolean evenWeek = ScheduleParser.getWeekParity();
        int timeRange = 0; // Длительность свободного времени

        Map<String, String> keysMap = KeysReader.readKeys(args);

        // Значение фильтра свободного времени
        if(keysMap.containsKey("-f")){
            try {
                timeRange = Integer.valueOf(keysMap.get("-f"));
            } catch (NumberFormatException e){
                e.printStackTrace();
                return "Неверный формат ключа -f";
            }
        }

        // Четность недели
        if(keysMap.containsKey("-p")){
            String parity = keysMap.get("-p");
            switch (parity) {
                case "0":
                    evenWeek = false;
                    break;
                case "1":
                    evenWeek = true;
                    break;
                default:
                    return "Неверно указан параметр ключа -p";
            }
        }

        // Имя группы
        if(keysMap.containsKey("-g")){
            group = keysMap.get("-g");
        }

        // Все расписание
        if (keysMap.containsKey("-a") || keysMap.containsKey("--all")){

            // Проверяем был ли задан параметр "-p"
            if(keysMap.containsKey("-p")){
                try {
                    return p.formattedAllSchedule(group, evenWeek);
                } catch (IOException e) {
                    return e.toString();
                }
            }else{
                try {
                    return p.formattedAllSchedule(group);
                } catch (IOException e) {
                    return e.toString();
                }
            }
        }

        // через несколько дней
        if(keysMap.containsKey("-d")){
            try{
                day = Date.increaseDayOfWeek(dayOfWeek, Integer.valueOf(keysMap.get("-d")));
            } catch (NumberFormatException e){
                return "Неверно введен параметр ключа -d";
            }
            if ( ((dayOfWeek + Integer.valueOf(keysMap.get("-d"))) / 7) % 2 != 0){
                if (!keysMap.containsKey("-p")) {
                    evenWeek = !evenWeek;
                }
            }

        }

        // Свободные промежутки времени в аудитории
        if (keysMap.containsKey("-r") && !keysMap.containsKey("-l")){

            AuditoryParser ap = new AuditoryParser();
            String aud = keysMap.get("-r");

            if (!keysMap.containsKey("-p") && !keysMap.containsKey("-d")){
                return ap.getFormattedFreeTimes(aud, timeRange);
            }
            if (keysMap.containsKey("-p")){
                if (keysMap.containsKey("-d")){
                    return ap.getFormattedFreeTimes(aud, day, evenWeek, timeRange);
                } else{
                    return ap.getFormattedFreeTimes(aud, evenWeek, timeRange);
                }
            }
            else{
                if (keysMap.containsKey("-d")){
                    return ap.getFormattedFreeTimes(aud, day, timeRange);
                } else{
                    return ap.getFormattedFreeTimes(aud, timeRange);
                }
            }
        }

        // Список уроков в аудитории
        if (keysMap.containsKey("-l")){

            AuditoryParser ap = new AuditoryParser();
            if (!keysMap.containsKey("-r")){
                return "Не указан параметр -r номер аудитории";
            }
            String aud = keysMap.get("-r");
            if (!keysMap.containsKey("-p") && !keysMap.containsKey("-d")){
                return ap.getFormattedSchedule(aud);
            }
            if (keysMap.containsKey("-p")){
                if (keysMap.containsKey("-d")){
                    return ap.getFormattedSchedule(aud, day, evenWeek);
                } else{
                    return ap.getFormattedSchedule(aud, evenWeek);
                }
            }
            else{
                if (keysMap.containsKey("-d")){
                    return ap.getFormattedSchedule(aud, day);
                } else{
                    return ap.getFormattedSchedule(aud);
                }
            }
        }

        String schedule;

        try {
            schedule =  p.formattedDaySchedule(day, group, evenWeek);
        } catch (IOException e) {
            return e.toString();
        }

        if (schedule.equals("")){
            return "У вас на этот день нет пар!";
        }else {
            return schedule;
        }

    }
}
