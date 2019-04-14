package core.modules.queue;

import com.google.gson.GsonBuilder;
import core.modules.Database;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author Arthur Kupriyanov
 */
public class QueueDB extends Database {
    private final String NAME = "name";
    private final String DATA = "data";
    private final String TYPE = "type";

    private final Connection connection;
    public QueueDB(){
        Database database = new Database();
        this.connection = database.getConnection();
    }

    public void saveQueue(Queue q) throws SQLException {
        String name = q.getQueueName();
        String type = q.type;
        String sql;
        PreparedStatement stmt;
        String data = QueueObject.convertQueueToJSON(q);

        if (checkExist(name)){
            sql = "UPDATE queue SET data=?::jsonb WHERE name=?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, data);
            stmt.setString(2, name);
        } else {
            sql = "INSERT INTO queue (name, data, type) VALUES (?,?::jsonb,?)";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, data);
            stmt.setString(3, type);
        }
        stmt.execute();
    }

    public boolean checkExist(String name) throws SQLException {
        String sql = "SELECT name FROM queue";
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);

        while(resultSet.next()){
            if (name.equals(resultSet.getString(NAME).trim())){
                return true;
            }
        }

        return false;
    }

    public SimpleQueue getSimpleQueue(String name) throws SQLException {
        String data = getData(name);
        if (data==null){
            return null;
        }
        QueueObject qo = new GsonBuilder().create().fromJson(data, QueueObject.class);
        return QueueBuilder.createSimpleQueue(qo);
    }

    public String getData(String queueName) throws SQLException {
        String sql = "SELECT data FROM queue WHERE name=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, queueName);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getString(DATA);

        return null;
    }

    public ArrayList<SimpleQueue> getSimpleQueueList() throws SQLException {
        String sql = "SELECT * FROM queue";

        ArrayList<SimpleQueue> queues = new ArrayList<>();

        ResultSet rs = connection.createStatement().executeQuery(sql);

        while(rs.next()){
            String type = rs.getString(TYPE);
            if (type.trim().equals("simple")){
                queues.add(getSimpleQueue(rs.getString(NAME)));
            }
        }

        return queues;

    }

}
