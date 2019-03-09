package core.modules;

import core.common.LocaleMath;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Arthur Kupriyanov
 */
@SuppressWarnings("Duplicates")
public class UsersDB implements AutoCloseable{

    // ИМЕНА КОЛОНОК

    private final String FIRST_NAME = "first_name";
    private final String LAST_NAME = "last_name";
    private final String VKID = "vkid";
    private final String GROUP_NAME = "group_name";
    private final String LOGIN = "login";
    private final String PASSWORD = "password";

    private Connection connection;

    public UsersDB(){
        Database database = new Database();
        this.connection = database.getConnection();
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

    /**
     * Получение логина с помощью VK ID
     * @param vkid идентификатор пользователя в VK
     * @return логин пользователя
     */
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

    /**
     * Получение пароля пользователя с помощью логина
     * @param login логин пользователя
     * @return пароль
     */
    public String getUserPassword(String login) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT login, password" +
                " FROM users";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            String loginDB = resultSet.getString(LOGIN).replace(" ","");
            String password = resultSet.getString(PASSWORD).replace(" ", "");

            if (loginDB.equals(login)){
                return password;
            }
        }

        return null;
    }

    /**
     * Получение VK ID с помощью логина
     * @param login логин, по которому производиться поиск
     * @return VK ID пользователя
     */
    public int getVKIDByLogin(String login) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT vkid, login" +
                " FROM users";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            int dbVKID = resultSet.getInt(VKID);
            String loginDB = resultSet.getString(LOGIN).replace(" ", "");

            if (loginDB.equals(login)){
                return dbVKID;
            }
        }

        return -1;
    }

    /**
     * Получение карты VKID и номера группы
     * @return Карта VKID:номер_группы
     */
    public HashMap<Integer, String> getVKIDListWithGroup() throws SQLException {
        HashMap<Integer, String> result = new HashMap<>();
        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT vkid, group_name" +
                " FROM users";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            result.put(resultSet.getInt(VKID), resultSet.getString(GROUP_NAME).trim());
        }

        return result;
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
     * Обновление логина пользователя через его VK ID
     * @param login логин пользователя
     * @param VKID идентификатор в VK
     */
    public void updateUserLogin(String login, int VKID) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "UPDATE users SET login='"+login+"' WHERE vkid="+VKID;
        statement.execute(sql);
    }

    /**
     * Генерирует пароль для пользователя и сохраняет его в базу данных
     * @param login логин пользователя
     * @return сгенерированныйпароль
     */
    public String generateUserPassword(String login) throws SQLException {
        Statement statement = connection.createStatement();
        String newPassword = generatePassword();
        String sql = "UPDATE users SET password='"+newPassword+"'  WHERE login='"+login + "'";
        statement.execute(sql);
        return newPassword;
    }

    /**
     * Удаление\очистка поля пароля пользователя
     * @param vkid идентификатор пользователя в VK
     */
    public void deleteUserPassword(int vkid) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "UPDATE users SET password=null  WHERE vkid="+vkid;
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

    /**
     * Проверяет есть ли у пользователя логин
     * <code>true</code> если он есть, иначе, в том числе, если пользователя с таким
     * vkid нет - <code>false</code>
     * @param vkid идентификатор пользователя в ВК
     * @return <code>true</code> если логин есть, иначе <code>false</code>
     * @throws SQLException
     */
    public boolean isAuthorized(int vkid) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT login, vkid FROM users");
        while(resultSet.next()){
            int dbVKID = resultSet.getInt(VKID);
            if (dbVKID == vkid) {
                try {
                    String loginDB = resultSet.getString(LOGIN).replace(" ", "");
                } catch (NullPointerException e) {
                    return false;
                }
                return true;
            }
        }

        return false;
    }

    /**
     * Проверка на наличие у пользователя пароля с использованием логина
     * <br> <code>true</code> - если пароль есть, во всех остальных,
     * в том числе, если пользователь с таким логином не найден - <code>false</code>
     * @param login логин пользователя
     * @return <code>true</code> - пароль есть, иначе <code>false</code>
     */
    public boolean checkPassword(String login) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT login, password FROM users");
        while(resultSet.next()){
            String loginDB = resultSet.getString(LOGIN).replace(" ","");
            if (loginDB.equals(login)) {
                try {
                    resultSet.getString(PASSWORD);
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
    public boolean checkUserExist(int vkid) throws SQLException {
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

    /**
     * Проверяет существует ли такой логин
     * @param login логин пользователя
     * @return <code>true</code> если такой логин есть, иначе <code>false</code>
     */
    public boolean checkUserExist(String login) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT login" +
                " FROM users";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            try {
                String loginDB = resultSet.getString(LOGIN).trim();
                if ( loginDB.equals(login)){
                    return true;
                }
            } catch (NullPointerException ignored){
            }

        }
        return false;
    }


    // ************** AUTHENTICATION **********************


    /**
     * -2 - пользователь не авторизован (отсутствует логин)
     * -1 - пользователь не найден (нет в базе данных)
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

            if (loginDB.equals(login) && passwordDB.equals(password)){
                return dbVKID;
            }
        }
        return -1;
    }

    private String generatePassword(){
        StringBuilder password = new StringBuilder();
        for(int i=0; i < 6; i++){
            password.append(LocaleMath.randInt(0, 9));
        }
        return password.toString();
    }

    public static void main(String[] args) throws SQLException {
        UsersDB usersDB = new UsersDB();
        //usersDB.addUser("Arthur","Kupriyanov",1200, "P3112");
        //usersDB.addUser("Daniel","Marshall",1222, "P3113");

        //usersDB.deleteUserPassword(1222);
        System.out.println(usersDB.checkPassword("marshall"));
        System.out.println(usersDB.generateUserPassword("marshall"));
        System.out.println(usersDB.checkPassword("marshall"));
        if (usersDB.checkPassword("marshall")) {
            System.out.println(usersDB.getUserPassword("marshall"));
        }
        usersDB.closeConnection();

    }

    @Override
    public void close() throws Exception {
        closeConnection();
    }
}
