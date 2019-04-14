package core.modules.modes.quest;

import core.modules.Database;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class QuestionsDB extends Database {
    private Connection connection;
    public QuestionsDB(){
        connection = new Database().getConnection();
    }

    public Map<Integer, String> getQuestions(String tag) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT question, id FROM questions WHERE quest_tag=?");
        stmt.setString(1, tag);
        ResultSet rs = stmt.executeQuery();

        HashMap<Integer, String> quest = new HashMap<>();

        while(rs.next()){
            quest.put(rs.getInt("id"),rs.getString("question"));
        }

        return quest;
    }
    public Map<Integer, String> getAllQuestions() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT question, id FROM questions");

        HashMap<Integer, String> quest = new HashMap<>();

        while(rs.next()){
            quest.put(rs.getInt("id"),rs.getString("question"));
        }

        return quest;
    }
    public String getById(int id) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT question FROM questions WHERE id="+id);

        if (rs.next()) return rs.getString("question");
        else return null;

    }

    public boolean checkUserExist(int vkid) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT vkid" +
                " FROM users_questions_data";
        ResultSet resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
            int dbVKID = resultSet.getInt("vkid");
            if (dbVKID == vkid){
                return true;
            }
        }
        return false;
    }

    public SavedQuestions getSavedQuestions(int vkid) throws SQLException {
        if (!checkUserExist(vkid)) return null;

        String sql = "SELECT saved_questions FROM users_questions_data WHERE vkid="+vkid;
        ResultSet rs = this.connection.createStatement().executeQuery(sql);

        if (rs.next()){
            return SavedQuestions.getInstanceFromJSON(rs.getString("saved_questions"));
        }
        return null;
    }

    public void saveQuestions(SavedQuestions sq, int vkid) throws SQLException {
        if (sq.getAll().isEmpty()){
            String sql = "DELETE FROM users_questions_data WHERE vkid=" + vkid;
            this.connection.createStatement().execute(sql);
            return;
        }
        if (!checkUserExist(vkid)) {
            String sql = "INSERT INTO users_questions_data (vkid, saved_questions) VALUES (?,?::jsonb)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, vkid);
            ps.setString(2, sq.toJSON());
            ps.execute();
        } else {
            String sql = "UPDATE users_questions_data SET saved_questions=?::jsonb";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, sq.toJSON());
            ps.execute();
        }

    }
    public static void main(String[] args) throws SQLException {

    }
}
