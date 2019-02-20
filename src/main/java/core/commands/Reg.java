package core.commands;

import core.common.KeysReader;
import core.common.UserInfoReader;
import core.modules.UsersDB;

import java.sql.SQLException;
import java.util.Map;

/**
 * @version 1.0
 * @author Arthur Kupriyanov
 */
public class Reg extends Command {
    public static void main(String[] args) {
        try {
            UsersDB usersDB = new UsersDB();
            System.out.println(usersDB.getFullNameByVKID(255396611));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String init(String... args) {
        Map<String, String> keysMap = KeysReader.readKeys(args);

        int vkid = Integer.valueOf(UserInfoReader.readUserID(args));
        String name = UserInfoReader.readUserFirstName(args);
        String lastname = UserInfoReader.readUserLastName(args);
        String group;
        if (keysMap.containsKey("-g")){
            group = keysMap.get("-g");
        } else return "Укажите вашу группу с ключом -g";

        try {
            UsersDB usersDB = new UsersDB();
            if (usersDB.checkUserExsist(vkid)){
                return "Вы уже зарегестрированы под именем " + usersDB.getFullNameByVKID(vkid);
            }
            usersDB.addUser(name, lastname,vkid,group);
            usersDB.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Ошибка при работе с базой данных: " + e.toString();
        }

        return "Вы успешно добавлены в базу данных";

    }

    @Override
    protected void setName() {
        this.name = "reg";
    }
}
