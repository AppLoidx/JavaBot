package core.modules.server.messages;

import com.google.gson.GsonBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Arthur Kupriyanov
 */
public class MessageGetter extends Messages{
    public Content getMessages(int chatId) throws SQLException {
        String sql = "SELECT messages FROM messages WHERE chat_id="+chatId;
        ResultSet rs = this.connection.createStatement().executeQuery(sql);

        if (rs.next()){
            String messages = rs.getString("messages");
            return new GsonBuilder().create().fromJson(messages, Content.class);
        } else return null;
    }

    public int getMaxMessageId(int chatId) throws SQLException{
        String sql = "SELECT max_msg_id FROM messages WHERE chat_id="+chatId;
        ResultSet rs = this.connection.createStatement().executeQuery(sql);
        if (rs.next()) return rs.getInt("max_msg_id");
        return -1;
    }
}
