package core.modules;

import vk.VKManager;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * @author Arthur Kupriyanov
 */
public class Auth {
    public boolean newAuth(String login, String description) throws SQLException {
        UsersDB usersDB = new UsersDB();
        if (!usersDB.checkUserExist(login)){
            return false; // Not Found
        }
        int vkid = usersDB.getVKIDByLogin(login);
        usersDB.deleteUserPassword(vkid);
        String password = usersDB.generateUserPassword(login);
        String message = description + "\n" + password;
        new VKManager().sendMessage(message, vkid);
        usersDB.closeConnection();
        return true; // Successful
    }

    private boolean checkPassword(String login, String password) throws SQLException {

        UsersDB usersDB = new UsersDB();
        if (!usersDB.checkUserExist(login) || !usersDB.checkPassword(login)) {
            usersDB.closeConnection();
            return false;
        }
        if (usersDB.getUserPassword(login).equals(password)) {
            usersDB.closeConnection();
            return true;
        }
        usersDB.closeConnection();
        return false;
    }

    /**
     * Метод авторизации с проверкой полей логина и пароля
     * @param login логин
     * @param password сгенерированный пароль
     * @return результат авторизации успешный - <code>true</code>, иначе - <code>false</code>
     */
    public int login(String login, String password) throws SQLException {
        if (checkPassword(login, password)){
            UsersDB usersDB = new UsersDB();
            return usersDB.auth(login, password);
        }
        return -1;
    }

    public void closeSession(String login, String password) throws SQLException {
        if (checkPassword(login, password)){
            UsersDB usersDB = new UsersDB();
            usersDB.deleteUserPassword(usersDB.getVKIDByLogin(login));
        }
    }

    public static void main(String[] args) throws SQLException {
        Auth auth = new Auth();
        System.out.println(auth.newAuth("myLogin", "This is your code:"));
        Scanner scanner = new Scanner(System.in);

        System.out.println(auth.login("myLogin", scanner.nextLine()));

    }
}
