package core.commands.spam;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import core.commands.ServiceCommand;
import core.modules.Date;
import core.modules.UsersDB;
import core.modules.parser.ScheduleParser;
import core.modules.parser.WeatherParser;
import core.modules.res.MenheraSprite;
import vk.VKManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author Arthur Kupriyanov
 */
public class MorningSpam implements ServiceCommand {


    public void morningSpam(){
        UsersDB usersDB = new UsersDB();
        try {
            HashMap<Integer, String> users = usersDB.getVKIDList();
            for(int key : users.keySet()){
                sendMorningSpam(key, users.get(key));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            usersDB.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void sendMorningSpam(int vkid, String group){
        UserXtrCounters user_info = new VKManager().getUserInfo(vkid);
        String user_name = user_info.getFirstName();
        String weather;
        try {
            weather = new WeatherParser().getWeatherTodayDescription();
        } catch (IOException e) {
            weather = "погоду не удалось получить...";
        }
        String schedule;
        try {
            schedule = new ScheduleParser()
                    .formattedDaySchedule(Date.getDayOfWeek() ,group,ScheduleParser.getWeekParity());
        } catch (IOException e) {
            schedule = "расписание получить не удалось...";
        }

        String scheduleAdditionalData;
        String parity = ScheduleParser.getWeekParity() ? "четная" : "нечетная";
        scheduleAdditionalData = "Группа: " + group + "\n" +
                "Четность: " + parity + "\n";

        String msg = "C добрым утром, " + user_name +
                "\n\n" + weather + "\n\n" + "Вот расписание. Не забудь все проверить! " +
                "Я ведь тоже могу ошибаться.\n\n" + scheduleAdditionalData + schedule + "\n\n" +
                "Удачи сегодня на парах!";

        try {
            new VKManager().getSendQuery()
                    .peerId(vkid)
                    .message(msg)
                    .attachment(MenheraSprite.GOOD_MORNING_SPRITE)
                    .execute();
        } catch (ApiException | ClientException ignored) {

        }

    }

    public static void main(String[] args) {
        MorningSpam ed = new MorningSpam();
        ed.morningSpam();

    }

    @Override
    public void service() {
        morningSpam();
    }
}
