package core.modules;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Arthur Kupriyanov
 */
public class Notifications {

    public boolean addNotification(String notification, int authorID){
        try {
            NotificationsDB notificationsDB = new NotificationsDB();
            notificationsDB.newNotification(notification, authorID);
            notificationsDB.closeConnection();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getFormattedNotifications() {
        try {
            NotificationsDB notificationsDB = new NotificationsDB();
            ArrayList<Notification> notifications = notificationsDB.getNotification();

            StringBuilder sb = new StringBuilder();
            for (Notification notification : notifications
            ) {
                sb.append("\n");
                sb.append(notification.message);
                sb.append("\n_____________");
                sb.append("\nAuthor: ").append(getNameByVKID(notification.authorID));
                sb.append("\nDate: ").append(notification.date);
                sb.append("\n===============================\n");
            }

            String response = sb.toString();
            if (response.equals("")){
                return "Объявлений нет";
            }

            return response;
        } catch (SQLException e){
            return "Ошибка при работе с базами данных: " + e.toString();
        }
    }

    private String getNameByVKID(int vkid) throws SQLException {
        UsersDB usersDB = new UsersDB();
        return usersDB.getFullNameByVKID(vkid);
    }

    public boolean checkExsist(int id){
        try {
            NotificationsDB notificationsDB = new NotificationsDB();
            return notificationsDB.checkExsist(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteNotification(int id){
        try {
            NotificationsDB notificationsDB = new NotificationsDB();
            notificationsDB.deleteNotification(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
