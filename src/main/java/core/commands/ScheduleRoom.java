package core.commands;

import core.common.KeysReader;
import core.modules.Date;
import core.modules.parser.itmo.schedule.AuditoryParser;
import core.modules.parser.itmo.schedule.ScheduleParser;

import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class ScheduleRoom{

    public String init(String... args) {

        int dayOfWeek = Date.getDayOfWeek();
        boolean evenWeek = ScheduleParser.getWeekParity();
        int timeRange = 0; // Длительность свободного времени


        Map<String, String> keysMap = KeysReader.readKeys(args);
        // Свободные промежутки времени в аудитории
        if (keysMap.containsKey("-r") && !keysMap.containsKey("-l")){

            if (keysMap.containsKey("-f")){
                try {
                    timeRange = Integer.valueOf(keysMap.get("-f"));
                } catch (NumberFormatException e){
                    return "Неверный формат для ключа -f";
                }
            }
            AuditoryParser ap = new AuditoryParser();
            String aud = keysMap.get("-r");
            if (aud.equals("")) return "Ключ -r без зачения";

            if (!keysMap.containsKey("-p") && !keysMap.containsKey("-d")){
                return ap.getFormattedFreeTimes(aud, timeRange);
            }
            if (keysMap.containsKey("-p")){
                if (keysMap.containsKey("-d")){
                    return ap.getFormattedFreeTimes(aud, dayOfWeek, evenWeek, timeRange);
                } else{
                    return ap.getFormattedFreeTimes(aud, evenWeek, timeRange);
                }
            }
            else{
                if (keysMap.containsKey("-d")){
                    return ap.getFormattedFreeTimes(aud, dayOfWeek, timeRange);
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
            String aud;
            if ((aud = keysMap.get("-r")).equals("")){
                return "Вы ввели пустой ключ -r. Укажите номер аудитории";
            }
            if (!keysMap.containsKey("-p") && !keysMap.containsKey("-d")){
                return ap.getFormattedSchedule(aud);
            }
            if (keysMap.containsKey("-p")){
                evenWeek = keysMap.get("-p").equals("1");
                if (keysMap.containsKey("-d")){
                    try {

                        int day = Integer.valueOf(keysMap.get("-d"));
                        if (day<0 || day > 7){
                            throw new NumberFormatException();
                        }
                        return ap.getFormattedSchedule(aud, day, evenWeek);
                    } catch (NumberFormatException e){
                        return "Введите верный формат для от 0 до 7";
                    }

                } else{
                    System.out.println(ap.getFormattedSchedule("304", evenWeek));
                    return ap.getFormattedSchedule(aud, evenWeek);
                }
            }
            else{
                if (keysMap.containsKey("-d")){
                    try {

                        int day = Integer.valueOf(keysMap.get("-d"));
                        if (day<0 || day > 7){
                            throw new NumberFormatException();
                        }
                        return ap.getFormattedSchedule(aud, day);
                    } catch (NumberFormatException e){
                        return "Введите верный формат для от 0 до 7";
                    }
                } else{
                    return ap.getFormattedSchedule(aud);
                }
            }
        }

        return "Проверьте правильность введенных данных";
    }

}
