package core.modules.server.messages;

import core.modules.Database;

import java.sql.Connection;

/**
 * @author Arthur Kupriyanov
 */
public class Messages {
    protected Connection connection;
    {
        this.connection = Database.getConnection();
    }
}
