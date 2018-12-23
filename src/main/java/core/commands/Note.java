package core.commands;

import core.common.KeysReader;
import core.modules.TeachersNotesDB;

import java.sql.SQLException;
import java.util.Map;

/**
 * Команда для записок
 *
 * ключи: [-t] [-kgdna]
 *
 * @version 1.0.0
 * @author Arthur Kupriyanov
 */
public class Note extends Command {

    // src/main/botResources/database/teachers_notes.db
    private TeachersNotesDB teachersNotesDB = new TeachersNotesDB();

    @Override
    public String init(String... args) {
        Map<String, String> keysMap = KeysReader.readKeys(args);
        String note;
        String key;
        if (keysMap.containsKey("-t")){

            if (keysMap.containsKey("-k")){
                key = keysMap.get("-k");
            }else{
                return "Не введен ключ -k - ключевое слово к записке";
            }

            if (keysMap.containsKey("-g")){
                try {
                    return teachersNotesDB.getNote(key);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(keysMap.containsKey("-d")){
                try {
                    if(teachersNotesDB.checkHasKey(key)){
                    teachersNotesDB.deleteNote(key);
                    return "Успешно удалена запись с ключом " + key;}
                    else{
                        return "Записи с таким ключом не существует";
                    }

                } catch (SQLException | ClassNotFoundException e) {
                    return "Произошла ошибка : " + e.toString();
                }
            }else if(keysMap.containsKey("-n")){
                note = keysMap.get("-n");

                try {
                    if( keysMap.containsKey("-a")){

                        if (teachersNotesDB.checkHasKey(key)){
                            teachersNotesDB.appendNote(key, note);
                            return "Успешна добавлена запись в ключ " + key;}
                        else{
                            teachersNotesDB.addNote(key, note);
                            return "Указанный ключ не найден, поэтому создана новая запись с ключом " + key;
                        }
                    }else if(teachersNotesDB.checkHasKey(key)){
                        return "Такой ключ уже существует. Удалите прошлую запись, или выполните " +
                                "операцию с ключом -a (append)";
                    }

                    teachersNotesDB.addNote(key, note);

                    return "Успешно добавлена запись с ключом "+key;

                } catch (SQLException | ClassNotFoundException e) {
                    return "Произошла ошибка : " + e.toString();
                }
            }else{
                return "Не введен ключ -n - запись";
            }
        }

        return "Убедитесь в правильности введенных данных";
    }

    @Override
    protected void setName() {
        name = "note";
    }
}
