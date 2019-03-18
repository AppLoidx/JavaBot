package core.modules.session;

import com.vk.api.sdk.objects.messages.Message;

/**
 * @author Arthur Kupriyanov
 */
public class SessionInputHandler {
    private Session session;
    private String response;
    private int vkid;

    public SessionInputHandler(int vkid){
        session = SessionManager.getSession(vkid);
        this.vkid = vkid;
    }

    public SessionInputHandler input(Message message){
        if (checkSystemCmd(message.getBody())) return this;
        response = session.newInput(message);
        return this;
    }

    public SessionInputHandler input(String msg){
        if (checkSystemCmd(msg)) return this;
        response = session.newInput(msg);
        return this;
    }

    public String get(){
        return response;
    }

    /**
     *
     * @param msg
     * @return <code>true</code> - если прерывание, если нет то <code>false</code>
     */
    public boolean checkSystemCmd(String msg){
        switch (msg.trim()){
            case "!session-close":
                String sessionName = session.getMode().getName();
                SessionManager.deleteSession(vkid);
                response = "Вы вышли из сессии " + sessionName;
                return true;
                default:
                    return false;
        }

    }
}
