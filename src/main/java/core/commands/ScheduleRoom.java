package core.commands;

import core.common.KeysReader;
import core.modules.Date;
import core.modules.parser.AuditoryParser;
import core.modules.parser.ScheduleParser;

import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class ScheduleRoom extends Command{
    @Override
    public String init(String... args) {

        int dayOfWeek = Date.getDayOfWeek();
        boolean evenWeek = ScheduleParser.getWeekParity();
        int timeRange = 0; // Длительность свободного времени

        Map<String, String> keysMap = KeysReader.readKeys(args);
        // Свободные промежутки времени в аудитории
        if (keysMap.containsKey("-r") && !keysMap.containsKey("-l")){

            AuditoryParser ap = new AuditoryParser();
            String aud = keysMap.get("-r");

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
            String aud = keysMap.get("-r");
            if (!keysMap.containsKey("-p") && !keysMap.containsKey("-d")){
                return ap.getFormattedSchedule(aud);
            }
            if (keysMap.containsKey("-p")){
                if (keysMap.containsKey("-d")){
                    return ap.getFormattedSchedule(aud, dayOfWeek, evenWeek);
                } else{
                    return ap.getFormattedSchedule(aud, evenWeek);
                }
            }
            else{
                if (keysMap.containsKey("-d")){
                    return ap.getFormattedSchedule(aud, dayOfWeek);
                } else{
                    return ap.getFormattedSchedule(aud);
                }
            }
        }

        return "Проверьте правильность введенных данных";
    }

    @Override
    protected void setConfig() {
        commandName = "scheduleRoom";
    }
}
