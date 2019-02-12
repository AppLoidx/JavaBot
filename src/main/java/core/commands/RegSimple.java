package core.commands;

import core.common.KeysReader;
import core.modules.UsersDB;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class RegSimple extends Command{
    @Override
    public String init(String... args) {
        Map<String, String> keysMap = KeysReader.readKeys(args);

        int isuID = 0;
        String groupName = null;

        if (keysMap.containsKey("-i")){
            try {
                isuID = Integer.valueOf(keysMap.get("-i"));
            } catch (NumberFormatException e){
                e.printStackTrace();
                return "Неверный формат ключа [-i] [int_номер_ису]";
            }
        }
        if (keysMap.containsKey("-g")){
            groupName = keysMap.get("-g").toUpperCase();
        }

        String firstName = keysMap.get("--#first_name");
        String lastName = keysMap.get("--#last_name");
        int userID = Integer.valueOf(keysMap.get("--#user_id"));

        UsersDB usersDB = new UsersDB();
        try {
            if (usersDB.checkExistByVKID(userID)){
                return "Вы уже есть в базе данных"; // TODO: добавить функцию для обновления
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return e.getMessage();
        }

        try {
            usersDB.addUser(firstName,lastName, isuID, userID, groupName);
            return "Вы успешно добавлены в базу данных:\n" +
                    "Имя: " + firstName +
                    "\nФамилия: " + lastName +
                    "\nНомер вк: " + userID;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Ошибка при работе с базой данных " + e.getMessage();
        }

    }

    @Override
    protected void setName() {
        this.name = "reg";
    }
}
