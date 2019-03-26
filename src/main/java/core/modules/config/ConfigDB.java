package core.modules.config;

import core.modules.Database;

import java.sql.*;

/**
 * @author Arthur Kupriyanov
 */
public class ConfigDB extends Database {
    private static final Connection connection = getConnection();

    public boolean saveSettings(int vkid, String json){
        PreparedStatement ps;
        try {
            if (checkExist(vkid)) {
                System.out.println(json);
                ps = connection.prepareStatement("UPDATE user_configs SET settings=?::jsonb WHERE vkid=?");
                ps.setInt(2, vkid);
                ps.setString(1,json);
                ps.execute();
            }else {
                ps = connection.prepareStatement("INSERT INTO user_configs VALUES (?,?::jsonb)");
                ps.setInt(1, vkid);
                ps.setString(2, json);
                ps.execute();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

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

    public String getSettings(int vkid){

            try {
                if (!checkExist(vkid)){
                    setDefaultSettings(vkid);
                }

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT settings FROM user_configs WHERE vkid="+vkid);
                rs.next();
                String settings = rs.getString("settings");
                if (settings==null){
                    setDefaultSettings(vkid);
                    return getSettings(vkid);
                } else return settings;

            } catch (SQLException e) {
                e.printStackTrace();
            }

        return null;
    }

    private void setDefaultSettings(int vkid){
        Settings defaultSettings = new DefaultSettings().set(new Settings());
        defaultSettings.setVkid(vkid).save();
    }

}
