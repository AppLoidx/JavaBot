package core.commands;

import core.common.KeysReader;
import core.modules.UsersDB;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class Reg extends Command {

    @Override
    public String init(String... args) {
        Map<String, String> keysMap = KeysReader.readKeys(args);

        int vkid;
        int isuID;
        String name;
        String lastname = null;
        String group = null;

        if(keysMap.containsKey("-i")){
            try {
                isuID = Integer.valueOf(keysMap.get("-i"));
            } catch (NumberFormatException e){
                return "Неверный формат для ключа -i";
            }
        }else{
            return "Укажите ваш номер ИСУ с ключом -i";
        }
        if (keysMap.containsKey("-g")){
            group = keysMap.get("-g");
        }
        if(keysMap.containsKey("-n")){
            String fullname = keysMap.get("-n");

            if(fullname.split("-").length > 1){
                name = fullname.split("-")[0];
                lastname = fullname.split("-")[1];
            }else{
                name = fullname;
            }
        }else{
            return "Введите имя в формате:\nИМЯ-ФАМИЛИЯ с ключом -n";
        }

        UsersDB db = new UsersDB();
        if(keysMap.containsKey("-v")){
            try {

                // Проверка на существование уже существкющего номера ВК
                vkid = Integer.valueOf(keysMap.get("-v"));

                try {
                    if (db.checkExistByVKID(vkid)){
                        return "Пользователь с таким номером ВК уже существует";
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    db.addUser(name,lastname,isuID,vkid,group);
                } catch (SQLException e) {
                    return "Ошибка добавления в базу данных, проверьте введнные вами данные";
                }

                return String.format("Вы успешно добавлены в базу данных, проверьте ваши данные\n" +
                        "Имя: %s\n" +
                        "Фамилия: %s\n" +
                        "Номер ИСУ: %s\n" +
                        "Номер ВК: %s\n" +
                        "Группа: %s\n",
                        name,lastname,isuID,vkid,group);

            } catch (NumberFormatException e){
                return "Неверный формат для ключа -v";
            }
        } else{
            return "Введите ваш номер ВК с ключом -v";
        }
    }

    @Override
    protected void setName() {
        this.name = "reg";
    }
}
