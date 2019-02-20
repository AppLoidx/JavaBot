package core.commands;

import core.common.KeysReader;
import core.common.UserInfoReader;
import core.modules.Date;
import core.modules.UsersDB;
import core.modules.parser.ScheduleParser;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Артур Куприянов
 * @version 1.4
 */
public class Schedule extends Command{


    @Override
    protected void setName() {
        name = "schedule";
    }

    private ScheduleParser p = new ScheduleParser();

    /**
     * Ключи: <br>
     *     -a - все расписание
     *     -f {value} - минимальная длительность свободного времени <br>
     *     -p - четность [1 - четная, 0 - нечетная] <br>
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
        String group;
        System.out.println(getGroup(args));
        if ((group=getGroup(args))==null){
            group = "P3112";
        }

        int day = dayOfWeek;
        boolean evenWeek = ScheduleParser.getWeekParity();
        int timeRange = 0; // Длительность свободного времени

        Map<String, String> keysMap = KeysReader.readKeys(args);

        if (keysMap.containsKey("-r")){
            return new ScheduleRoom().init(args);
        }

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

        String schedule;

        try {
            schedule =  p.formattedDaySchedule(day, group, evenWeek);
        } catch (IOException e) {
            return e.toString();
        }

        if (schedule.equals("")){
            return "У вас на этот день нет пар!";
        }else {
            String scheduleAdditionalData;
            String parity = evenWeek ? "четная" : "нечетная";
            scheduleAdditionalData = "Группа: " + group + "\n" +
                    "Четность: " + parity + "\n";
            return scheduleAdditionalData + schedule;
        }

    }

    String getGroup(String ... args){
        String vkid = UserInfoReader.readUserID(args);
        try {
            UsersDB usersDB = new UsersDB();

            String group = usersDB.getGroupByVKID(Integer.valueOf(vkid));

            return group;
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}
