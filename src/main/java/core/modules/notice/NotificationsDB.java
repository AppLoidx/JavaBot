package core.modules.notice;

import core.modules.Date;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author Arthur Kupriyanov
 */
public class NotificationsDB {
    final String NOTIFICATION = "notification";
    final String AUTHOR_ID = "author_id";
    final String PRIMARY_KEY = "primary_key";
    final String DATE = "date";

    private Connection connection;

    public NotificationsDB() throws SQLException {
        String dbUrl = "jdbc:postgresql://ec2-176-34-113-195.eu-west-1.compute.amazonaws.com:5432/d4t7ailnb47b9s";
        String log = "?user=oqxaigahmtousk&password=b17bc25f436815d846e34717c7f23e412513eb92ee7344d5ceaf4d1469b1873d";
        String additionalConfig = "&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        this.connection = DriverManager.getConnection(dbUrl + log + additionalConfig);
    }

    public void newNotification(String notification, int authorID) throws SQLException {
        Statement stmt;
        stmt = connection.createStatement();

        String sql = String.format("INSERT INTO notifications(notification, author_id, date) VALUES ('%s', %d, '%s')", notification, authorID, Date.getDate());

        stmt.execute(sql);
    }

    public ArrayList<Notification> getNotification() throws SQLException {
        Statement stmt;
        ArrayList<Notification> notifications = new ArrayList<>();

        stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT notification, author_id, primary_key, date FROM notifications");
        while(resultSet.next()) {
            Notification notification = new Notification(resultSet.getString(NOTIFICATION),
                    resultSet.getInt(AUTHOR_ID), resultSet.getInt(PRIMARY_KEY), resultSet.getString(DATE));
            notifications.add(notification);
        }
        return notifications;
    }
    public Notification getNotification(int id) throws SQLException {
        Statement stmt;

        stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT notification, author_id, primary_key, date FROM notifications");
        while(resultSet.next()) {
            if (resultSet.getInt(PRIMARY_KEY) == id) {

                return new Notification(resultSet.getString(NOTIFICATION),
                        resultSet.getInt(AUTHOR_ID), resultSet.getInt(PRIMARY_KEY), resultSet.getString(DATE));
            }
        }
        return null;
    }

    public void deleteNotification(int id) throws SQLException {
        Statement stmt = connection.createStatement();
        String sql ="DELETE FROM notifications WHERE primary_key=" + id;
        stmt.execute(sql);
    }

    public boolean checkExsist(int id) throws SQLException {
        Statement stmt = connection.createStatement();
        String sql ="SELECT * FROM notifications WHERE primary_key=" + id;
        ResultSet resultSet = stmt.executeQuery(sql);
        return resultSet.next();
    }
    public void closeConnection() throws SQLException {
        connection.close();
    }

}
