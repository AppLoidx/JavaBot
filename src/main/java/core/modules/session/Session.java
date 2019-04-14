package core.modules.session;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.Mode;

/**
 * @author Arthur Kupriyanov
 */
public class Session {
    private final int vkid;
    private final Mode mode;
    private final Thread sessionThread;

    public Session(int vkid, Mode mode, UserIOStream input, UserIOStream output){
        this.vkid = vkid;
        this.mode = mode;
        this.mode.setInput(input);
        this.mode.setOutput(output);
        this.mode.setUserID(vkid);
        this.sessionThread = new Thread(mode);
        this.sessionThread.setName("Thread-" + mode.getName() + "-vkid-" + vkid);
    }

    public Mode getMode(){
        return this.mode;
    }

    public String newInput(String input){
        return mode.getResponse(input);
    }

    public String newInput(Message message){
        return mode.getResponse(message);
    }

    public UserIOStream getInputStream(){
        return this.mode.getInputStream();
    }

    public UserIOStream getOutputStream(){
        return this.mode.getOutputStream();
    }

    public int getVkid(){
        return vkid;
    }

    void destoryThread(){
        this.sessionThread.interrupt();
    }

    public void run(){
        this.sessionThread.start();
    }

    public Thread getThread(){
        return sessionThread;
    }
}
