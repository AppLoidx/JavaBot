package core.modules;

import java.sql.*;
import java.util.ArrayList;

/**
 * @author Arthur Kupriyanov
 */
public class QuestionsDB extends Database {
    Connection connection;
    public QuestionsDB(){
        connection = new Database().getConnection();
    }

    public ArrayList<String> getQuestions() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT question FROM questions");

        ArrayList<String> quest = new ArrayList<>();

        while(rs.next()){
            quest.add(rs.getString("question"));
        }

        return quest;
    }

    public ArrayList<String> getQuestions(String tag) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT question FROM questions WHERE quest_tag=?");
        stmt.setString(1, tag);
        ResultSet rs = stmt.executeQuery();

        ArrayList<String> quest = new ArrayList<>();

        while(rs.next()){
            quest.add(rs.getString("question"));
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
