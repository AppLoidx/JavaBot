package core.commands.spam;

import core.modules.Date;
import core.modules.parser.ScheduleParser;
import core.modules.parser.WeatherParser;

import java.io.IOException;

/**
 * @author Arthur Kupriyanov
 */
class SpamDataGetter {

    static String getSchedule(String group){
        return getSchedule(group, 0);

    }

    static String getSchedule(String group, int increase){
        String schedule;
        try {
            schedule = new ScheduleParser()
                    .formattedDaySchedule(Date.increaseDayOfWeek(Date.getDayOfWeek(),increase),
                            group,
                            ScheduleParser.getWeekParity(increase));
        } catch (IOException e) {
            schedule = "расписание получить не удалось...";
        }

        return schedule;
    }

    static String getWeather(){
        String weather;
        try {
            weather = new WeatherParser().getWeatherTodayDescription();
        } catch (IOException e) {
            weather = "погоду не удалось получить...";
        }

        return weather;
    }
}
