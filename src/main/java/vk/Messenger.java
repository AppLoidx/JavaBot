package vk;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import core.Commander;
import core.modules.session.SessionInputHandler;
import core.modules.session.SessionManager;
import core.modules.session.UserIOStream;

public class Messenger implements Runnable{
    private GroupActor actor;
    private VkApiClient vk;
    private Message message;
    private int peerId;

    public Messenger(GroupActor actor, VkApiClient vk, Message message){
        this.actor = actor;
        this.vk = vk;
        this.message = message;
        this.peerId = message.getUserId();
    }

    /**
     * Обращается к классу {@link Commander} сначала с аргументом типа {@link Message},
     * если такой команды не найдется (выполнится команда {@link core.commands.Unknown},
     * которая возвращает null - выполнится обычная команда с методом init(String) c
     * с дополнинением к сигнатуре метаданных
     *
     * @return В случае успешного выполнения - строку, в случае, если команда не
     * нуждается в автоматической отправке ответа ( либо он осуществляется внутри
     * самой программы)  - <code>""</code>. Исключительным случаем является возвращение
     * ответа от команды {@link core.commands.Unknown}, которая возвращает строковый ответ о том,
     * что команда не найдена.
     * @see Commander
     * @see core.CommandDeterminant
     * @see core.commands.Unknown
     */
    private String getResponse(){

        if (SessionManager.checkExist(message.getUserId())){
            return new SessionInputHandler(message.getUserId()).input(message).get();
        }

        String vkResponse =  Commander.getResponse(message);
        if (vkResponse != null ){
            if (vkResponse.equals("")){
                return null;
            }
            return vkResponse;
        } else {
            String extra = "";
            extra += " --#user_id " + message.getUserId();
            UserXtrCounters info = getUserInfo(message.getUserId());
            extra += " --#first_name " + info.getFirstName();
            extra += " --#last_name " + info.getLastName();

            return Commander.getResponse(message.getBody() + extra);
        }

    }

    private void sendMessage(String msg){
        if (msg == null){
            System.out.println("null");
            return;
        }
        try {
            this.vk.messages().send(this.actor).peerId(peerId).message(msg).execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получаем ответ методом {@link #getResponse()} и в случае, если он
     * не <code>null</code> - отправляем как простое сообщение.
     */
    @Override
    public void run() {
        String response = getResponse();
        if (response!=null) {
            sendMessage(response);
        }
    }

    /**
     * Обращается к VK API и получает объект, описывающий пользователя.
     * @param id идентификатор пользователя в VK
     * @return {@link UserXtrCounters} информацию о пользователе
     * @see UserXtrCounters
     */
    private UserXtrCounters getUserInfo(int id){
        try {
            return vk.users()
                    .get(actor)
                    .userIds(String.valueOf(id))
                    .execute()
                    .get(0);
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return null;
    }
}
