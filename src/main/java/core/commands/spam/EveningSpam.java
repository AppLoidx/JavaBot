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
public class EveningSpam implements ServiceCommand {
    @Override
    public void service() {
        evenSpam();
    }

    private void evenSpam(){
            UsersDB usersDB = new UsersDB();
            try {
                HashMap<Integer, String> users = usersDB.getVKIDList();
                for(int key : users.keySet()){
                    sendEvenSpam(key, users.get(key));
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

    private void sendEvenSpam(int vkid, String group){
        UserXtrCounters user_info = new VKManager().getUserInfo(vkid);
        String user_name = user_info.getFirstName();

        String schedule;
        try {
            schedule = new ScheduleParser().formattedDaySchedule(Date.increaseDayOfWeek(Date.getDayOfWeek(),1),group,ScheduleParser.getWeekParity());
        } catch (IOException e) {
            schedule = "расписание получить не удалось...";
        }

        String scheduleAdditionalData;
        String parity = ScheduleParser.getWeekParity() ? "четная" : "нечетная";
        scheduleAdditionalData = "Группа: " + group + "\n" +
                "Четность: " + parity + "\n";

        String msg = "Добрейший вечерочек, " + user_name +
                "\n\n" + "Вот расписание на завтра. Не забудь все проверить! " +
                "Может быть я что-то перепутала?.\n\n" + scheduleAdditionalData + schedule + "\n\n" +
                "Не забудь сделать домашку!\n\nХорошего вечера!";

        try {
            new VKManager().getSendQuery()
                    .peerId(vkid)
                    .message(msg)
                    .attachment(MenheraSprite.CHILL_SPRITE)
                    .execute();
        } catch (ApiException | ClientException ignored) {

        }
    }

    public static void main(String[] args) {
        EveningSpam e = new EveningSpam();
        e.sendEvenSpam(255396611, "P3112");
    }
}
