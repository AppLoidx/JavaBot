package core.modules;

import java.sql.*;

/**
 * @author Arthur Kupriyanov
 */
public class TeachersNotesDB extends SQLiteDB{
    @Override
    public void setURL() {
        url = "jdbc:sqlite:C:/java/Bot/src/main/botResources/database\\teachers_notes.db";
    }

    Connection connection;
    {
        try {
            connection = getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkHasKey(String key) throws SQLException, ClassNotFoundException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM students");
        while(resultSet.next()){
            if (key.equals(resultSet.getString("name"))) {
                return true;
            }
        }
        return false;
    }

    public void addNote(String key, String newNote) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO students(name, note) VALUES(?,?)";

        //Connection conn = this.getConnection(url);
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, key);
        pstmt.setString(2, newNote);

        pstmt.executeUpdate();

    }
    public void appendNote(String key, String note) throws SQLException {
        String sql = "UPDATE main.students SET note = ? WHERE name = ?";

        //Connection conn = this.getConnection(url);
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, getNote(key) + " " + note);
        pstmt.setString(2, key);

        pstmt.executeUpdate();
    }

    public String getNote(String key) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM students");
        while(resultSet.next()){
            if (key.equals(resultSet.getString("name"))) {
                return resultSet.getString("note");
            }
        }
        return "None";
    }

    public void deleteNote(String key) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM main.students WHERE name =?";

        //Connection conn = this.getConnection(url);
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, key);
        pstmt.executeUpdate();

    }
}
