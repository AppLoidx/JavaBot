package core.modules;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * @author Arthur Kupriyanov
 */
public class Database {
    private static ThreadLocal<Connection> connection;

    static {
        Properties dbConfig = new Properties();
        String dbUrl;
        try {
            dbConfig.load(new FileReader(new File("src/main/java/core/modules/herokuDatabaseConfig.properties")));
            dbUrl = dbConfig.getProperty("dbURL")
                    + dbConfig.getProperty("log")
                    + dbConfig.getProperty("config");

        } catch (IOException e) {
            Map<String, String> env = System.getenv();
            dbUrl = env.get("JDBC_DATABASE_URL");
        }

        try {
            connection.set( DriverManager.getConnection(dbUrl));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getLocalConnection(){
        return connection.get();
    }

    public static Connection getConnection(){
        Properties dbConfig = new Properties();
        String dbUrl;
        try {
            dbConfig.load(new FileReader(new File("src/main/java/core/modules/herokuDatabaseConfig.properties")));
            dbUrl = dbConfig.getProperty("dbURL")
                    + dbConfig.getProperty("log")
                    + dbConfig.getProperty("config");

        } catch (IOException e) {
            Map<String, String> env = System.getenv();
            dbUrl = env.get("JDBC_DATABASE_URL");
        }
        try {
            return DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
