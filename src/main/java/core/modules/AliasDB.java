package core.modules;

import com.google.gson.GsonBuilder;

import java.sql.*;

/**
 * @author Arthur Kupriyanov
 */
public class AliasDB {
    private Connection connection = Database.getConnection();

    public void addAlias(int vkid, Aliases aliases) throws SQLException {

        boolean exist = checkExist(vkid);
        if (exist && aliases.isEmpty()){
            Statement stmt = connection.createStatement();
            stmt.execute("DELETE FROM alias WHERE vkid="+vkid);
        }

        if (exist){
            PreparedStatement stmt = connection.prepareStatement("UPDATE alias SET aliases=?::jsonb WHERE vkid=?");
            stmt.setString(1, aliases.toJSON());
            stmt.setInt(2, vkid);
            stmt.execute();
        } else {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO alias (vkid, aliases) VALUES (?,?::jsonb)");
            stmt.setInt(1, vkid);
            stmt.setString(2, aliases.toJSON());
            stmt.execute();
        }
    }
    public boolean checkExist(int vkid) throws SQLException {
            Statement statement;
            statement = connection.createStatement();
            String sql = "SELECT vkid" +
                    " FROM alias";
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                int dbVKID = resultSet.getInt("vkid");
                if (dbVKID == vkid){
                    return true;
                }
            }
            return false;
        }
        public Aliases getAlias(int vkid) throws SQLException {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT aliases FROM alias WHERE vkid="+vkid);
            if (rs.next()){
                String json = rs.getString("aliases");
                return new GsonBuilder().create().fromJson(json, Aliases.class);
            }
            return null;
        }
}
