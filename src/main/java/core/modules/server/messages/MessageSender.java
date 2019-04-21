package core.modules.server.messages;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Arthur Kupriyanov
 */
public class MessageSender extends Messages {
    public void addMessage(String message, int chatId, int userId) throws SQLException {
        Message msg = new Message().setChatId(chatId).setUserId(userId).setMessage(message);
        boolean exist = checkExist(chatId);
        String sql;
        if (exist) sql = "UPDATE messages SET messages=?::jsonb, max_msg_id=? WHERE chat_id=?";
        else{
            sql = "INSERT INTO messages (chat_id, messages, max_msg_id) VALUES (?,?::jsonb,?)";
        }
        PreparedStatement ps = this.connection.prepareStatement(sql);

        if (exist){
            Content content = new MessageGetter().getMessages(chatId);
            if (content==null) content = new Content(chatId);
            content.addMessage(msg);
            ps.setString(1, content.toJSON());
            ps.setInt(2, content.getMaxMsgId());
            ps.setInt(3, chatId);
        } else {
            Content content = new Content(chatId);
            content.addMessage(msg);
            ps.setInt(1, chatId);
            ps.setString(2, content.toJSON());
            ps.setInt(3, content.getMaxMsgId());
        }

        ps.execute();

    }

    public void createChat(int chatId) throws SQLException {
        String sql = "INSERT INTO messages (chat_id, max_msg_id) VALUES (?,?)";
        PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setInt(1, chatId);
        ps.setInt(2, 0);
        ps.execute();
    }

    public boolean checkExist(int chatId) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT chat_id" +
                " FROM messages";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            int dbVKID = resultSet.getInt("chat_id");
            if (dbVKID == chatId){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws SQLException {
        MessageSender ms = new MessageSender();

        ms.addMessage("another2222  message", 123123, 2222);

    }
}
