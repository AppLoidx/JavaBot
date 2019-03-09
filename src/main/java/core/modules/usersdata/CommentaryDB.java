package core.modules.usersdata;

import core.modules.Database;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class CommentaryDB extends Database {

    private final String VKID = "vkid";
    private final String TYPE = "user_type";
    private final String COMMENTARY = "commentary";
    private final String STATISTIC = "statistic";

    private final String JSON_COMMENTARY = "commentary";


    private Connection connection;

    {
        Database db = new Database();
        this.connection = db.getConnection();
    }

    private boolean checkUserExist(int vkid) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT vkid" +
                " FROM users_data";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            int dbVKID = resultSet.getInt(VKID);
            if (dbVKID == vkid) {
                return true;
            }
        }
        return false;
    }

    private Type getUserType(int vkid) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT vkid, user_type" +
                " FROM users_data";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            int dbVKID = resultSet.getInt(VKID);
            if (dbVKID == vkid) {
                return Type.getType(resultSet.getString(TYPE).trim());
            }
        }
        return null;
    }

    /**
     * @param teacherID      ID teacher
     * @param studentID students VK ID
     * @return private comment for this ID teacher
     */
    public String getPrivateComment(int teacherID, int studentID) throws SQLException {
        if (getUserType(teacherID) != Type.TEACHER) {
            return null;
        }
        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT vkid, commentary" +
                " FROM users_data";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            int dbVKID = resultSet.getInt(VKID);
            if (dbVKID == teacherID) {
                try {
                    Map<Integer, HashMap<String, String>> commentary = parseJSON(resultSet.getString(COMMENTARY));
                    for (int id : commentary.keySet()){
                        if (id == studentID){
                            return commentary.get(id).get(JSON_COMMENTARY);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        return null;
    }

    public Map<Integer, String> getPrivateComments(int teacherID) throws SQLException {
        if (getUserType(teacherID) != Type.TEACHER) {
            return null;
        }
        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT vkid, commentary" +
                " FROM users_data";
        ResultSet resultSet = statement.executeQuery(sql);

        Map<Integer, String> response = new HashMap<>();
        while (resultSet.next()) {
            int dbVKID = resultSet.getInt(VKID);
            if (dbVKID == teacherID) {
                try {
                    Map<Integer, HashMap<String, String>> commentary = parseJSON(resultSet.getString(COMMENTARY));
                    for (int id : commentary.keySet()){
                        response.put(id, commentary.get(id).get(JSON_COMMENTARY));
                    }

                    if (response.isEmpty()) return null;
                    return response;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        return null;
    }

    /**
     * @param vkid ID student
     * @return public comment for student
     */
    public Map<Integer, String> getPublicComment(int vkid) throws SQLException {
        if (getUserType(vkid) != Type.STUDENT) {
            return null;
        }
        Statement statement;
        statement = connection.createStatement();
        String sql = "SELECT vkid, commentary" +
                " FROM users_data";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            int dbVKID = resultSet.getInt(VKID);
            if (dbVKID == vkid) {
                try {
                    Map<Integer, String> comments = new HashMap<>();
                    Map<Integer, HashMap<String, String>> commentary = parseJSON(resultSet.getString(COMMENTARY));

                    for (Integer key : commentary.keySet()) {
                        comments.put(key, commentary.get(key).get(JSON_COMMENTARY));
                    }
                    if (comments.isEmpty()) return null;
                    return comments;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        return null;
    }

    public void setPrivateComment(int teacherID, int studentID, String comment) throws SQLException {
        if (!checkUserExist(teacherID)){
            createUser(teacherID, Type.TEACHER);
        }

        PreparedStatement statement =
                this.connection.prepareStatement(
                        "UPDATE users_data SET commentary=?::jsonb WHERE vkid=?");

        Map<Integer, HashMap<String, String>>  comments = getCommentaries(teacherID);
        if (comments == null){
            comments = new HashMap<>();
        }
        HashMap<String, String> studentsComment = new HashMap<>();
        if (comment == null){
            comments.remove(studentID);
        } else {
            studentsComment.put("commentary", comment);
            comments.put(studentID, studentsComment);
        }
        String commentary = new JSONObject().toJSONString(comments);
        statement.setString(1, commentary);
        statement.setInt(2,teacherID);

        statement.execute();
    }

    public void setPublicComment(int teacherID, int studentID, String comment) throws SQLException {
        if (!checkUserExist(studentID)){
            createUser(studentID, Type.STUDENT);
        }

        PreparedStatement statement =
                this.connection.prepareStatement(
                        "UPDATE users_data SET commentary=?::jsonb WHERE vkid=?");

        Map<Integer, HashMap<String, String>>  comments = getCommentaries(studentID);
        if (comment == null){
            comments.remove(teacherID);
        } else {
            HashMap<String, String> studentsComment = new HashMap<>();
            studentsComment.put("commentary", comment);
            comments.put(teacherID, studentsComment);
        }

        String commentary = new JSONObject().toJSONString(comments);
        statement.setString(1, commentary);
        statement.setInt(2, studentID);
        statement.execute();
    }

    private Map<Integer, HashMap<String, String>> getCommentaries(int vkid) throws SQLException {
        Statement statement = this.connection.createStatement();
        String sql = "SELECT vkid, commentary" +
                " FROM users_data";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            int dbVKID = resultSet.getInt(VKID);
            if (dbVKID == vkid) {
                try {
                    Map<Integer, HashMap<String, String>> comments = parseJSON(resultSet.getString(COMMENTARY));
                    if (comments == null){
                        comments = new HashMap<>();
                    }

                    return comments;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return new HashMap<>();
                }
            }
        }

        return new HashMap<>();
    }

    private void createUser(int vkid, Type type) throws SQLException {
        if (checkUserExist(vkid)){
            return;
        }
        PreparedStatement statement =
                this.connection.prepareStatement("INSERT INTO users_data(vkid, user_type) VALUES (?,?) ");

        statement.setInt(1,vkid);
        statement.setString(2, type.getType());

        statement.execute();
    }

    private Map<Integer, HashMap<String, String>> parseJSON(String s) throws ParseException {
        if (s==null){
            return null;
        }
        Map<String, HashMap<String, String>> object = (Map<String, HashMap<String, String>>) new JSONParser().parse(s);
        Map<Integer, HashMap<String, String>> map = new HashMap<>();
        for(String key : object.keySet()){
            map.put(Integer.valueOf(key), object.get(key));
        }
        return  map;
    }

    public static void main(String[] args) throws SQLException {
        CommentaryDB commentaryDB = new CommentaryDB();
        commentaryDB.setPrivateComment(255396611, 402109451, "дополнить исключениями");
        commentaryDB.setPrivateComment(255396611, 207109089, "неправильная реализация");
        commentaryDB.setPrivateComment(255396611, 34321005, "итератор");

    }

}
