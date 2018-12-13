package core.modules.LinksDB;

import core.modules.SQLiteDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Arthur Kupriyanov
 */
public class LinksDB extends SQLiteDB {
    @Override
    public void setURL() {
        url = "jdbc:sqlite:C:/java/Bot/src/main/botResources/links.db";
    }

    /**
     * Возвращает ссылку по ключу из базы данных links.db
     * @param key ключ по которому ищется ссылка
     * @return Ссылка или документ
     * @throws SQLException работа с SQL, не обработана
     * @throws ClassNotFoundException работа  с SQL, не обработана
     */
    public String getLinkByKey(String key) throws SQLException, ClassNotFoundException {
        Statement stmt = getConnection(url).createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM links");
        while(resultSet.next()){
            if (key.equals(resultSet.getString("key"))) {
                return resultSet.getString("link");
            }
        }
        return null;
    }
    public String getNameByKey(String key) throws SQLException, ClassNotFoundException {
        Statement stmt = getConnection(url).createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM links");
        while(resultSet.next()){
            if (key.equals(resultSet.getString("key"))) {
                return resultSet.getString("name");
            }
        }
        return null;
    }

}
