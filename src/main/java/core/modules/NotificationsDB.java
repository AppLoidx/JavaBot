package core.modules;

import java.sql.*;

/**
 * @author Arthur Kupriyanov
 */
public class NotificationsDB extends SQLiteDB{
    @Override
    public void setURL() {
        this.url = "jdbc:sqlite:C:/java/Bot/src/main/botResources/database/notifications.db";
    }

    private Connection conn;

    {
        try {
            conn = this.getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void newNotification(String notice, String userId) throws SQLException {
        String sql = "INSERT INTO notifications(notification, author_id) VALUES(?,?)";

        PreparedStatement pstmt = this.conn.prepareStatement(sql);
        pstmt.setString(1, notice);
        pstmt.setString(2, userId);

        pstmt.executeUpdate();
    }

    public String getNotifications() throws SQLException {
        Statement stmt = this.conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM notifications");
        StringBuilder sb = new StringBuilder();
        while(resultSet.next()){
            sb.append(String.format("<notice author=%s>",resultSet.getString("author_id")));
                sb.append(resultSet.getString("notification"));
                sb.append("</notice>");
            }
        return sb.toString();
    }
}

