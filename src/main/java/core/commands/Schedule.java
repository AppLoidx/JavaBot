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
public class Schedule extends Command implements Helpable{


    @Override
    protected void setConfig() {
        commandName = "schedule";
    }

    private ScheduleParser p = new ScheduleParser();

    /**
     * Ключи: <br>
     *     -a - все расписание <br>
     *     -p - четность [1 - четная, 0 - нечетная] <br>
     *     -g {value} - группа <br>
     *     -d {value} - количество дней на перед <br>
     *     -r {value} - номер аудитории <br>
     * @param args ввод пользователя разделенный на массив через пробел <br>
     * @return Ответ пользователю
     */
    @Override
    public String init(String... args) {

        int dayOfWeek = Date.getDayOfWeek();

        // DEFAULT VALUES
        String group;

        int day = dayOfWeek;
        boolean evenWeek = ScheduleParser.getWeekParity();

        Map<String, String> keysMap = KeysReader.readKeys(args);

        /*
        Парсим имя группы из сообщения или из базы данных с помощью vk id,
        если нету ничего из них - выводит ошибку. Следует заметить, что
        указание ключа приоритетнее, чем группа выбранная по умолчанию из БД
         */
        if (keysMap.containsKey("-g")) {
            group = keysMap.get("-g");
        } else {
            if ((group=getGroup(args))==null) {
                return "Вы не указали номер группы и вас нет в базе данных.\n" +
                        "Укажите номер вашей группы через ключ -g или зарегистрируйтесь, " +
                        "с помощью команды Reg";
            }
        }


        if (keysMap.containsKey("-r")){
            return new ScheduleRoom().init(args);
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

    @Override
    public String getManual() {
        return "Эта команда может работать в двух режимах:\n" +
                "По аудиториям (с ключом -r [номер_комнаты])\n" +
                "По группам (без ключа -r)\n" +
                "\nПо аудиториям:" +
                "-g [номер_группы] - если вы не укзали номер группы, то оно берется из базы данных, " +
                "если вы зарегистрированы.\n" +
                "-d [int] = покажет расписние на int дней вперед\n" +
                "-p [1 или 0] - 1 - четная неделя; 0 - нечетная\n" +
                "-a = вывести все недельное расписание\n" +
                "\nПо комнатам:" +
                "Обязательный ключ: -r [номер_комнаты]\n" +
                "По умолчнию покажет промежуток свободных интервалов в комнате.\n" +
                "-f [int] = покажет свободные промежутки большие или равные значению int (в минутах)\n" +
                "-l = покажет лекции в этой аудитории\n" +
                "-p [1 или 0] - 1 - четная неделя; 0 - нечетная\n";
    }

    @Override
    public String getDescription() {
        return "Команда для вывода расписаний";
    }

    public static void main(String[] args) {
        Helpable h = new Schedule();
        System.out.println(h.getDescription());
        System.out.println(h.getManual());
    }
}
