package core.modules.custom.commands;

import core.modules.Database;

import java.sql.*;
import java.util.HashMap;

/**
 * @author Arthur Kupriyanov
 */
public class CustomCommandDB extends Database {
    Connection connection;
    public CustomCommandDB(){
        this.connection = new Database().getConnection();
    }

    public boolean checkExist(int vkid) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT vkid" +
                " FROM user_configs";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            int dbVKID = resultSet.getInt("vkid");
            if (dbVKID == vkid){
                return true;
            }
        }
        return false;
    }

    public CommandList getList(int vkid) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT commands" +
                " FROM user_configs WHERE vkid="+vkid;
        ResultSet rs = statement.executeQuery(sql);
        CommandList cl = null;
        if (rs.next()){
            cl = CommandListBuilder.build(rs.getString("commands"));
        }

        return cl;
    }

    public void addList(CommandList cl, int vkid) throws SQLException {
        String json = CommandListBuilder.convert(cl);
        PreparedStatement statement;
        if (!checkExist(vkid)) {
            String sql = "INSERT INTO user_configs (vkid, commands) VALUES (?,?::jsonb)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, vkid);
            statement.setString(2, json);
            statement.execute();
        } else {
            String sql = "UPDATE user_configs SET commands=?::jsonb WHERE vkid=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(2, vkid);
            statement.setString(1, json);
            statement.execute();
        }
    }

    public HashMap<Integer, CommandList> getAll() throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT commands, vkid" +
                " FROM user_configs";
        ResultSet rs = statement.executeQuery(sql);
        CommandList cl = null;

        HashMap<Integer, CommandList> res = new HashMap<>();

        while (rs.next()){
            cl = CommandListBuilder.build(rs.getString("commands"));
            res.put(rs.getInt("vkid"), cl);
        }

        return res;
    }
}
