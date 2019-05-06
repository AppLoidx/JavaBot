package core.modules.queuev2;

import com.google.gson.GsonBuilder;
import core.modules.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Arthur Kupriyanov
 */
public class QueueDB {

    private Connection connection;

    {
        connection = Database.getLocalConnection();
    }

    public void save(Queue q) throws SQLException {
        if (getNames().contains(q.getName())){
            update(q);
        } else {
            String sql = "INSERT INTO queue (name, data) VALUES (?, ?::jsonb)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, q.getName());
            stmt.setString(2, new GsonBuilder().create().toJson(q));
            stmt.execute();
        }
    }

    private synchronized void update(Queue q) throws SQLException {
        String sql = "UPDATE queue SET data=?::jsonb WHERE name=?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, new GsonBuilder().create().toJson(q));
        stmt.setString(2, q.getName());
        stmt.execute();
    }

    public Queue get(String name) throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery("SELECT data FROM queue WHERE name=\'" + name + "'");
        if (rs.next()) return new GsonBuilder().create().fromJson(rs.getString("data"), Queue.class);
        else return null;
    }

    public List<String> getNames() throws SQLException {
        List<String> res = new ArrayList<>();

        ResultSet rs = connection.createStatement().executeQuery("SELECT name FROM queue");
        while (rs.next()){
            res.add(rs.getString("name"));
        }

        return res;
    }

    public synchronized void delete(String name) throws SQLException {
        connection.createStatement().execute("DELETE FROM queue WHERE name='" + name + "'");
    }

}
