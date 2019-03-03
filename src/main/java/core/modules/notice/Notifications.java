package core.modules.notice;

import core.modules.UsersDB;
import org.json.simple.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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
            notificationsDB.closeConnection();

            StringBuilder sb = new StringBuilder();
            for (Notification notification : notifications
            ) {
                sb.append("\n");
                sb.append(notification.message);
                sb.append("\n_____________");
                sb.append("\nAuthor: ").append(getNameByVKID(notification.authorID));
                sb.append("\nDate: ").append(notification.date).append(" ID: ").append(notification.ID);
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

    public Stream<String> getNotificationsStream() {
        ArrayList<String> formattedNotifications = new ArrayList<>();
        try {
            NotificationsDB notificationsDB = new NotificationsDB();
            ArrayList<Notification> notifications = notificationsDB.getNotification();
            notificationsDB.closeConnection();

            for (Notification notification : notifications
            ) {
                StringBuilder sb = new StringBuilder();
                sb.append("ID: ").append(notification.ID).append("\n");
                sb.append(notification.message);
                sb.append("\n_____________");
                sb.append("\nAuthor: ").append(getNameByVKID(notification.authorID));
                sb.append("\nDate: ").append(notification.date);
                sb.append("\n===============================\n");
                if (!sb.toString().equals("")) {
                    formattedNotifications.add(sb.toString());
                }
            }

            if (formattedNotifications.isEmpty()){
                formattedNotifications.add("empty");
            }

            return formattedNotifications.stream();
        } catch (SQLException e){
            formattedNotifications.clear();
            formattedNotifications.add("error \n Ошибка при работе с базами данных: " + e.toString());
            return formattedNotifications.stream();
        }
    }

    /**
     * @return Объявления в формате JSON
     */
    public String getJSONNotifications(){
        try {
            NotificationsDB notificationsDB = new NotificationsDB();
            ArrayList<Notification> notifications = notificationsDB.getNotification();
            notificationsDB.closeConnection();

            JSONObject jsonObject = new JSONObject();

            int index = 0;
            for (Notification notification : notifications
            ) {
                jsonObject.put(String.valueOf(index++), convertToMap(notification));
            }

            return jsonObject.toString();

        } catch (SQLException e){
            return "Error";
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

    public int getAuthorID(int id) throws SQLException, NotificationNotFoundException {
        try{
            NotificationsDB notificationsDB = new NotificationsDB();
            Notification notice;
            if ((notice = notificationsDB.getNotification(id)) != null){
                return notice.authorID;
            } else {
                throw new NotificationNotFoundException();
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public void deleteNotification(int id){
        try {
            NotificationsDB notificationsDB = new NotificationsDB();
            notificationsDB.deleteNotification(id);
            notificationsDB.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> convertToMap(Notification notification){
        Map<String, String> noteMap = new HashMap<>();
        noteMap.put("message", notification.message);
        noteMap.put("date", notification.date);
        noteMap.put("authorID", String.valueOf(notification.authorID));
        noteMap.put("id", String.valueOf(notification.ID));

        return noteMap;
    }
}
