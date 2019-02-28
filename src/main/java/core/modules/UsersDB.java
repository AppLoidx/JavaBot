package core.modules;

import com.sun.javaws.exceptions.ExitException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * @author Arthur Kupriyanov
 */
@SuppressWarnings("Duplicates")
public class UsersDB {

    // ИМЕНА КОЛОНОК

    private final String FIRST_NAME = "first_name";
    private final String LAST_NAME = "last_name";
    private final String VKID = "vkid";
    private final String GROUP_NAME = "group_name";
    private final String LOGIN = "login";
    private final String PASSWORD = "password";

    private Connection connection;

    public UsersDB() throws SQLException{
        Properties dbConfig = new Properties();
        try {
            dbConfig.load(new FileReader(new File("src/main/java/core/modules/herokuDatabaseConfig.properties")));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Файл с конфигурацией базы данных не найден!");
            System.exit(ExitException.LAUNCH_ABORT_SILENT);
        }
        String dbUrl = dbConfig.getProperty("dbURL");
        String log = dbConfig.getProperty("log");
        String additionalConfig = dbConfig.getProperty("config");
        this.connection = DriverManager.getConnection(dbUrl + log + additionalConfig);
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }


    // ******************** GETTERS *****************************

    /**
     * Получает полное имя, используя номер ID от VK
     * @param vkid номер ID от VK
     * @return номер группы
     */
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

    /**
     * Получает номер группы, используя номер ID от VK
     * @param vkid номер ID от VK
     * @return номер группы
     */
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

    public String getLoginByVKID(int vkid) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT vkid, login" +
                " FROM users";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            int dbVKID = resultSet.getInt(VKID);
            String login = resultSet.getString(LOGIN).replace(" ", "");

            if (dbVKID == vkid){
                return login;
            }
        }

        return null;
    }


    // ******************** DATABASE UPDATERS ***************************


    /**
     * Добавление пользователя в базу данных
     * @param firstName Имя
     * @param lastname Фамилия
     * @param vkid номер от VK ID
     * @param groupName номер группы (необязательный параметр)
     */
    public void addUser(String firstName, String lastname, int vkid, String groupName) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        String sqlStatement = "INSERT INTO users (first_name, last_name, vkid, group_name)" +
                "VALUES ('" + firstName + "' , '" + lastname + "' , '" + vkid + "' , '" + groupName + "' );";

        statement.executeUpdate(sqlStatement);
    }

    /**
     * Обновление\Добавление Логина и Пароля у пользователя
     * @param login логин
     * @param password пароль
     * @param VKID номер ID от VK
     */
    public void updateUserLoginPassword(String login, String password, int VKID) throws SQLException {
        Statement statement = connection.createStatement();
        String generatedPassword = generatePassword(password, login);
        String sql = "UPDATE users SET login='"+login+"', password='"+generatedPassword+"' WHERE vkid="+VKID;
        statement.execute(sql);
    }

    /**
     * Обновление колонки группа в базе данных
     * @param group номер группы
     * @param vkid номер ID от VK
     */
    public void updateGroup(String group, int vkid) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE users SET group_name = ? WHERE vkid = ?");
        statement.setString(1,group);
        statement.setInt(2,vkid);
        statement.execute();
    }



    // ******************* CHECKERS *******************************



    public boolean isAuthorized(int vkid) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT login, password, vkid FROM users");
        while(resultSet.next()){
            int dbVKID = resultSet.getInt(VKID);
            if (dbVKID == vkid) {
                try {
                    String loginDB = resultSet.getString(LOGIN).replace(" ", "");
                    String passwordDB = resultSet.getString(PASSWORD).replace(" ", "");
                } catch (NullPointerException e) {
                    return false;
                }
                return true;
            }
        }

        return false;
    }

    /**
     * Проверяет существование пользователя через номер от VK ID
     * @param vkid номер VK ID
     * @return <code>true</code> если есть, иначе <code>false</code>
     */
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


    // ************** AUTHENTICATION **********************


    /**
     * -2 - пользователь не авторизован
     * -1 - пользователь не найден
     * @param login логин
     * @param password пароль
     * @return код ошибки или ID пользователя
     */
    public int auth(String login, String password) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT login, password, vkid FROM users");
        while(resultSet.next()){
            int dbVKID = resultSet.getInt(VKID);
            if (!isAuthorized(dbVKID)){
                continue;
            }
            String loginDB = resultSet.getString(LOGIN).replace(" ", "");
            String passwordDB = resultSet.getString(PASSWORD).replace(" ", "");

            if (loginDB.equals(login) && passwordDB.equals(generatePassword(password, login))){
                return dbVKID;
            }
        }
        return -1;
    }

    /**
     * Аутенфикация с дополнительной проверкой соответствия vkid
     * @param login логин
     * @param password пароль
     * @param vkid ИД от аккаунта ВК пользователя
     * @return <code>true</code> - если авторизация удачная, иначе <code>false</code>
     */
    public boolean auth(String login, String password, int vkid) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT login, password, vkid FROM users");
        if (!isAuthorized(vkid)){
            return false;
        }
        while(resultSet.next()){
            int dbVKID = resultSet.getInt(VKID);
            if (vkid == dbVKID) {
                String loginDB = resultSet.getString(LOGIN).replace(" ", "");
                String passwordDB = resultSet.getString(PASSWORD).replace(" ", "");

                if (loginDB.equals(login) && passwordDB.equals(generatePassword(password, login))) {
                    return true;
                }
            }
        }
        return false;
    }

    private String generatePassword(String password, String salt){
        return String.valueOf((password + salt).hashCode());
    }

    public static void main(String[] args) throws SQLException {
        UsersDB usersDB = new UsersDB();
        //usersDB.addUser("Arthur","Kupriyanov",1200, "P3112");
        //usersDB.addUser("Daniel","Marshall",1222, "P3113");
        System.out.println(usersDB.isAuthorized(1200));
        System.out.println(usersDB.auth("marshall","myLongPass"));
        usersDB.updateUserLoginPassword("marshall","myLongPass",1222);
        System.out.println(usersDB.auth("marshall","myLongPass"));
        usersDB.closeConnection();

    }
}
