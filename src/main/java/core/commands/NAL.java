package core.commands;

import com.vk.api.sdk.objects.messages.Message;

/**
 * @author Arthur Kupriyanov
 */
public class NAL {
    public static String getNatureResponse(Message message){
        String body = message.getBody().toLowerCase();

        if (body.equals("спасибо")){
            return "Не за что";
        }

        return null;
    }
}
