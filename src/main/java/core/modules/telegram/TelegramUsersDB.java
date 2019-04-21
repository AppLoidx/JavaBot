package core.modules.telegram;

import core.modules.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Arthur Kupriyanov
 */
public class TelegramUsersDB {
    private Connection connection = Database.getConnection();

    public Integer getVKID(int telegramId) throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery("SELECT vkid FROM telegram_users_db WHERE telegram_id=" + telegramId);
        if (rs.next()) return rs.getInt("vkid");
        else return null;
    }

    public void add(int telegramId, int vkid) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO telegram_users_db (telegram_id, vkid) VALUES (?,?)");
        stmt.setInt(1, telegramId);
        stmt.setInt(2, vkid);
        stmt.execute();
    }

    public boolean checkExist(int telegramId) throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery("SELECT vkid FROM telegram_users_db WHERE telegram_id=" + telegramId);
        if (rs.next()) return true;
        else return false;
    }
}
