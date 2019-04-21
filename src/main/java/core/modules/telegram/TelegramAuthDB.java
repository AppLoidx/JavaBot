package core.modules.telegram;

import core.common.LocaleMath;
import core.modules.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Arthur Kupriyanov
 */
public class TelegramAuthDB {
    private Connection connection = Database.getConnection();

    public void newReg(int telegramId, int vkid, int generatedPassword) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO telegram_auth (telegram_user_id, temp_password, vkid) VALUES (?,?,?)");
        stmt.setInt(1, telegramId);
        stmt.setInt(2, generatedPassword);
        stmt.setInt(3, vkid);
        stmt.execute();
    }

    public void delete(int telegramId) throws SQLException {
        connection.createStatement().execute("DELETE FROM telegram_auth WHERE telegram_user_id="+telegramId);
    }
    public Integer getVKID(int telegramId) throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery("SELECT vkid FROM telegram_auth WHERE telegram_user_id="+telegramId);
        if (rs.next()){
            delete(telegramId);
            return rs.getInt("temp_password");
        }
        return null;
    }
    public Integer getPassword(int telegramId) throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery("SELECT temp_password FROM telegram_auth WHERE telegram_user_id="+telegramId);
        if (rs.next()){
            delete(telegramId);
            return rs.getInt("temp_password");
        }
        return null;
    }
}
