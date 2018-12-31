package core.modules;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Arthur Kupriyanov
 */
public class QuestionDB extends SQLiteDB {
    Connection connection = getConnection(url);

    public QuestionDB() throws SQLException, ClassNotFoundException {
    }

    @Override
    public void setURL() {
        url = "jdbc:sqlite:C:/java/Bot/src/main/botResources/database/questions.db";
    }

    public void addQuestion(String newQuestion) throws SQLException {
        String sql = "INSERT INTO main.oop(question) VALUES(?)";

        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, newQuestion);

        pstmt.executeUpdate();
    }

    public static void main(String ... args){

    }
}
