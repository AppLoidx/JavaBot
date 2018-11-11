package core.commands;

import core.commands.enums.Mode;
import core.modules.Date;
import core.modules.parser.ScheduleParser;

import java.io.IOException;
import java.util.Map;

/**
 * @author Артур Куприянов
 */
public class Schedule extends Command{

    Schedule(){
        name = "schedule";
        mode = Mode.DEFAULT;
    }

    private ScheduleParser p = new ScheduleParser();

    /**
     *
     * @param args ввод пользователя разделенный на массив через пробел
     * @return Ответ пользователю
     */
    @Override
    public String init(String... args) {

        int dayOfWeek = Date.getDayOfWeek();

        // DEFAULT VALUES
        String group = "P3112";     //TODO: Get from user settings
        int day = dayOfWeek;
        boolean evenWeek = ScheduleParser.getWeekParity();

        Map<String, String> keysMap = KeysReader.readKeys(args);

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

        String schedule;
        try {
            schedule =  p.formattedDaySchedule(day, group, evenWeek);
        } catch (IOException e) {
            return e.toString();
        }

        if (schedule.equals("")){
            return "Убедитесь в правильности введнных данных";
        }else {
            return schedule;
        }

    }
}
