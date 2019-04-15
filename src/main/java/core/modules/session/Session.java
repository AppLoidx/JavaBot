package core.modules.session;

import com.vk.api.sdk.objects.messages.Message;
import core.commands.Mode;

import java.util.Date;

/**
 * @author Arthur Kupriyanov
 */
public class Session {
    private final int vkid;
    private final Mode mode;
    private final Thread sessionThread;
    private long lastOperationTime;

    public Session(int vkid, Mode mode, UserIOStream input, UserIOStream output){
        this.vkid = vkid;
        this.mode = mode;
        this.mode.setInput(input);
        this.mode.setOutput(output);
        this.mode.setUserID(vkid);
        this.sessionThread = new Thread(mode);
        this.sessionThread.setName("Thread-" + mode.getName() + "-vkid-" + vkid);
        this.lastOperationTime = new Date().getTime();
    }

    public Mode getMode(){
        return this.mode;
    }

    public String newInput(String input){
        updateLastOperationTime();
        return mode.getResponse(input);
    }

    public String newInput(Message message){
        updateLastOperationTime();
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

    public long getLastOperationTime(){
        return this.lastOperationTime;
    }

    private void updateLastOperationTime(){
        this.lastOperationTime = new Date().getTime();
    }
}
