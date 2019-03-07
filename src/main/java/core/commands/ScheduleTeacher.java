package core.commands;

import core.modules.Date;
import core.modules.parser.ScheduleParser;
import core.modules.parser.TeacherScheduleParser;

import java.io.IOException;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class ScheduleTeacher{

    public String init(Map<String, String> keysMap, String... args) {

        boolean evenWeek = ScheduleParser.getWeekParity();
        int day = Date.getDayOfWeek();
        TeacherScheduleParser tcp = new TeacherScheduleParser();

        // Номер ису
        int isuID;
        try {
            String id = new Schedule().getGroup(args);
            isuID = Integer.parseInt(id);

        } catch (NumberFormatException e){
            return "У вас не подходящий isu id. Если вы хотите увидеть учительское расписание, " +
                    "вместо group введите свой номер ИСУ";
        }

        // через несколько дней
        if(keysMap.containsKey("-d")){
            try{
                day = Date.increaseDayOfWeek(day, Integer.valueOf(keysMap.get("-d")));
                evenWeek = ScheduleParser.getWeekParity(Integer.valueOf(keysMap.get("-d")));
            } catch (NumberFormatException e){
                return "Неверно введен параметр ключа -d";
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

        // Все расписание
        if (keysMap.containsKey("-a") || keysMap.containsKey("--all")){

            // Проверяем был ли задан параметр "-p"
            if(keysMap.containsKey("-p")){
                try {
                    if (keysMap.containsKey("-d")){
                        return tcp.getFormattedSchedule(isuID, day, evenWeek);
                    } else return tcp.getFormattedSchedule(isuID, evenWeek);
                } catch (IOException e) {
                    return e.toString();
                }
            }else{
                try {
                    if (keysMap.containsKey("-d")){
                        return tcp.getFormattedSchedule(isuID, day);
                    } else return tcp.getFormattedSchedule(isuID);
                } catch (IOException e) {
                    return e.toString();
                }
            }

        }

        try {

            return tcp.getFormattedSchedule(isuID, day, evenWeek);
        } catch (IOException e) {
            return e.getMessage();
        }

    }

}
