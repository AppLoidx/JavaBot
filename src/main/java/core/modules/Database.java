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
    private Connection connection;
    {
        Properties dbConfig = new Properties();
        String dbUrl;
        String log;
        String additionalConfig;
        try {
            dbConfig.load(new FileReader(new File("src/main/java/core/modules/herokuDatabaseConfig.properties")));
            dbUrl = dbConfig.getProperty("dbURL");
            log = dbConfig.getProperty("log");
            additionalConfig = dbConfig.getProperty("config");
        } catch (IOException e) {
            Map<String, String> env = System.getenv();
            dbUrl = env.get("CUSTOM_DATABASE_URL");
            log = env.get("CUSTOM_DATABASE_LOG");
            additionalConfig = env.get("CUSTOM_DATABASE_CONFIG");
            System.out.println(env);
        }

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
