package core.modules;

import java.sql.*;

/**
 * @author Arthur Kupriyanov
 */
public class UsersDB extends SQLiteDB {

    @Override
    public void setURL() {
        this.url = "jdbc:sqlite:C:/java/Bot/src/main/botResources/database/users.db";
    }
    private Connection conn;

    {
        try {
            conn = this.getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(String name, String lastname, int isuID, int vkid, String group) throws SQLException {
        String sql = "INSERT INTO users(name, lastname, isu_id, vkid, groupName) VALUES(?,?,?,?,?)";

        PreparedStatement pstmt = this.conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, lastname);
            pstmt.setInt(3, isuID);
            pstmt.setInt(4,vkid);
            pstmt.setString(5, group);
        pstmt.executeUpdate();


    }

    public void deleteByVKID(int vkid) {
        String sql = "DELETE FROM main.users WHERE vkid = ?";

        try (Connection conn = this.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, vkid);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean checkExistByVKID(int vkid) throws SQLException, ClassNotFoundException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM users");
        while(resultSet.next()){
            if (vkid == resultSet.getInt("vkid")){
                return true;
            }
        }
        return false;

    }
    public String getFullNameByVKID(int vkid) throws SQLException, ClassNotFoundException {
        Statement stmt = this.conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM users");
        while(resultSet.next()){
            if (vkid == resultSet.getInt("vkid")){
                return resultSet.getString("lastname") + " " + resultSet.getString("name");
            }
        }
        return null;
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.conn.close();
    }

    public static void main(String ... args){

    }
}
