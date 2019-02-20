package core.modules;

import org.apache.logging.log4j.core.appender.db.jdbc.ConnectionSource;

import java.sql.*;

/**
 * @author Arthur Kupriyanov
 */
public class UsersDB {
    final String FIRST_NAME = "first_name";
    final String LAST_NAME = "last_name";
    final String VKID = "vkid";
    final String GROUP_NAME = "group_name";
    private Connection connection;

    public UsersDB() throws SQLException {
        String dbUrl = "jdbc:postgresql://ec2-176-34-113-195.eu-west-1.compute.amazonaws.com:5432/d4t7ailnb47b9s";
        String log = "?user=oqxaigahmtousk&password=b17bc25f436815d846e34717c7f23e412513eb92ee7344d5ceaf4d1469b1873d";
        String additionalConfig = "&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        this.connection = DriverManager.getConnection(dbUrl + log + additionalConfig);
    }

    public void addUser(String firstName, String lastname, int vkid, String groupName) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        String sqlStatement = "INSERT INTO users (first_name, last_name, vkid, group_name)" +
                "VALUES ('" + firstName + "' , '" + lastname + "' , '" + vkid + "' , '" + groupName + "' );";

        statement.executeUpdate(sqlStatement);
    }

    public boolean checkUserExsist(int vkid) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT vkid" +
                " FROM users";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            int dbVKID = resultSet.getInt(VKID);
            if (dbVKID == vkid){
                return true;
            }
        }
        return false;
    }
    public String getFullNameByVKID(int vkid) throws SQLException {

        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT vkid, first_name, last_name" +
                " FROM users";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            int dbVKID = resultSet.getInt(VKID);
            if (dbVKID == vkid){
                String firstName = resultSet.getString(FIRST_NAME).replace(" ","");
                String lastName = resultSet.getString(LAST_NAME).replace(" ","");
                return lastName + " " + firstName;
            }
        }

        return null;
    }
    public String getGroupByVKID(int vkid) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT vkid, group_name" +
                " FROM users";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            int dbVKID = resultSet.getInt(VKID);
            String groupName = resultSet.getString(GROUP_NAME).replace(" ", "");

            if (dbVKID == vkid){
                return groupName;
            }
        }

        return null;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
