package core.commands;

import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import core.commands.VKCommands.VKCommand;
import core.common.KeysReader;
import core.modules.UsersDB;
import core.modules.usersdata.CommentaryDB;
import vk.VKManager;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class Comment extends Command implements VKCommand {
    @Override
    protected void setConfig() {
        commandName = "comment";
    }

    @Override
    public String exec(Message message) {
        int userID = message.getUserId();
        boolean student = false;

        UsersDB usersDB = new UsersDB();
        try {
            if (usersDB.checkUserExist(userID)){
                String group = usersDB.getGroupByVKID(userID);
                if (group.matches("[a-zA-Z][0-9].*")){
                    student = true;
                }
            }
        } catch (SQLException e) {
            return "Ошибка при работе с БД " + e.getMessage();
        }
        Map<String, String> keyMap = KeysReader.readKeys(message.getBody());
        int id = 0;

        if (keyMap.containsKey("-i")){
            try {
                id = Integer.valueOf(keyMap.get("-i"));
            } catch (NumberFormatException e){
                return "Неверный формат ключа \"-i\" [id_пользователя]";
            }
        }

        if (keyMap.containsKey("-a")){
            String access = keyMap.get("-a");

            switch (access){
                case "private":
                    if (student){
                        return "Для студентов недоступны private комментарии";
                    }
                    if (keyMap.containsKey("-i")) return getPrivateComment(userID, id);
                    return  getPrivateComment(userID);
                case "public":
                    return getPublicComment(userID);
                default:
                        return "Введите ключ \'-a\' со значением private или public";
            }
        } else {
            return getPublicComment(userID);
        }
    }

    private String getPrivateComment(int userID){
        CommentaryDB db = new CommentaryDB();
        try {
            Map<Integer, String> comments = db.getPrivateComments(userID);

            return formatComments(comments);

        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    private String getPrivateComment(int userID, int studentID){
        CommentaryDB db = new CommentaryDB();
        try {
            String comment = db.getPrivateComment(userID, studentID);
            if (comment == null){
                return "Комментариев нет";
            }

            return comment;
        } catch (SQLException e) {
            return e.getMessage();
        }
    }

    private String getPublicComment(int userID){
        CommentaryDB db = new CommentaryDB();
        try{
            Map<Integer, String> comments = db.getPublicComment(userID);
            return formatComments(comments);

        } catch (SQLException e){
            return e.getMessage();
        }
    }

    private String formatComments(Map<Integer, String> comments){
        StringBuilder sb = new StringBuilder();
        if (comments == null){
            return "Комментариев нет";
        }
        for (int id : comments.keySet()){
            UserXtrCounters user = VKManager.getUserInfo(id);
            sb.append("--------------------\n");
            if (user==null){
                sb.append("Неизвестный пользователь\n");
            } else {
                sb.append(user.getLastName()).append(" ").append(user.getFirstName()).append("\n");
            }
            sb.append("comment: ").append(comments.get(id)).append("\n");
        }

        return sb.toString();
    }
}
