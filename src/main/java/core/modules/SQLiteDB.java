package core.modules;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Супер-класс для классов, работающих с SQLite
 *
 * @author Arthur Kupriyanov
 */
public abstract class SQLiteDB {
    String url;
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    {
        setURL();
    }

    public abstract void setURL();
    public static Connection getConnection(String url) throws ClassNotFoundException, SQLException {

        //String url = "jdbc:sqlite:C:/java/Bot/src/main/botResources/database/users.db";
        return DriverManager.getConnection(url);
}

}
