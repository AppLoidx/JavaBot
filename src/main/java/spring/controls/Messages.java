package spring.controls;

import com.google.gson.GsonBuilder;
import core.modules.server.messages.Content;
import core.modules.server.messages.MessageGetter;
import core.modules.server.messages.MessageSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

/**
 * @author Arthur Kupriyanov
 */
@RestController
@RequestMapping("messages")
public class Messages {
    @RequestMapping("/create")
    String createChat(@RequestParam("chat_id") int chatId){
        MessageSender ms = new MessageSender();

        try {
            if (!ms.checkExist(chatId)){
                ms.createChat(chatId);
                return "200";
            } else {
                return "404";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "500";
        }

    }

    @RequestMapping("/send")
    String send(@RequestParam("chat_id") int chat_id,
               @RequestParam("user_id") int user_id,
               @RequestParam("message") String message){

        MessageSender ms=  new MessageSender();
        try {
            if (ms.checkExist(chat_id)) {
                ms.addMessage(message, chat_id, user_id);
                return "200";
            } else {
                return "404";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "500";
        }
    }

    @RequestMapping("/get")
    String get(@RequestParam("chat_id") int chat_id){
        MessageGetter mg = new MessageGetter();
        try {
            Content content = mg.getMessages(chat_id);
            return new GsonBuilder().create().toJson(content);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
