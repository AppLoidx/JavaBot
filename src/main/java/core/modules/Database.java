package core.modules;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Arthur Kupriyanov
 */
public class Database {
    private Connection connection;
    {
        Properties dbConfig = new Properties();
        try {
            dbConfig.load(new FileReader(new File("src/main/java/core/modules/herokuDatabaseConfig.properties")));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Файл с конфигурацией базы данных не найден!");
            System.exit(4);
        }
        String dbUrl = dbConfig.getProperty("dbURL");
        String log = dbConfig.getProperty("log");
        String additionalConfig = dbConfig.getProperty("config");
        try {
            connection = DriverManager.getConnection(dbUrl + log + additionalConfig);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        return connection;
    }
}
