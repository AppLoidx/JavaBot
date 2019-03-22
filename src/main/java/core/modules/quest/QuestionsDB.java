package core.modules.quest;

import core.modules.Database;

import java.sql.*;
import java.util.ArrayList;
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

    public Map<Integer, String> getQuestions() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT question,id FROM questions");

        HashMap<Integer, String> quest = new HashMap<>();

        while(rs.next()){
            quest.put(rs.getInt("id"),rs.getString("question"));
        }

        return quest;
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

    public void addQuestion(String question, String tag) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO questions (question, quest_tag) VALUES (?,?)");
        statement.setString(1, question);
        statement.setString(2, tag);
        statement.execute();
    }

    public static void main(String[] args) throws SQLException {

    }
}
