package core.commands;

import com.vk.api.sdk.objects.messages.Message;
import core.modules.session.UserIOStream;

/**
 * @author Arthur Kupriyanov
 */
public interface Mode extends Runnable {
    void setUserID(int id);
    String getName();
    String getResponse(String input);
    String getResponse(Message message);

    default void onExit(){}
    void setOutput(UserIOStream output);
    void setInput(UserIOStream input);

    UserIOStream getInputStream();
    UserIOStream getOutputStream();
}
