package core.commands;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.VKCommands.VKCommand;
import core.modules.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Arthur Kupriyanov
 */
public class Link extends Command implements VKCommand, Helpable {
    @Override
    protected void setConfig() {
        commandName = "link";
    }

    @Override
    public String exec(Message message) {
        String[] msg = message.getBody().split(" ");
        if (msg.length < 2) return "Введите параметры!";
        Connection connection = new Database().getConnection();
        StringBuilder sb = new StringBuilder();
        for (String link: msg){
            if (link.equals("link")) continue;
            try {
                sb.append(link).append(": ");
                ResultSet rs = connection.createStatement().executeQuery("SELECT link FROM links WHERE name='"+link+"'");
                if (rs.next()) sb.append(rs.getString("link"));
                else sb.append("ссылка не найдена");
                sb.append("\n");
            } catch (SQLException e) {
                e.printStackTrace();
                sb.append("не удалось получить");
            }

        }
        if (sb.toString().equals("")) return null;
        else return sb.toString();
    }

    @Override
    public String getManual() {
        return "Ссылки для первого курса ПИиКТ\n" +
                "prog - программирование\n" +
                "opd - ОПД\n" +
                "\n" +
                "Чтобы получить ссылку введите\n" +
                "link <имя_ссылки>\n\n" +
                "Например, link opd";
    }

    @Override
    public String getDescription() {
        return "Получение ссылок";
    }
}
