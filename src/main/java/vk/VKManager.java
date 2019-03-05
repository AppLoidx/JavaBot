package vk;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;
import core.common.LocaleMath;
import core.modules.res.MenheraSprite;

import java.io.IOException;

/**
 * @author Arthur Kupriyanov
 */
public class VKManager {
    public static VKCore vkCore;

    static {
        try {
            vkCore = new VKCore();
        } catch (IOException | ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg, int peerId){
        if (msg == null){
            System.out.println("null");
            return;
        }
        try {
            vkCore.getVk().messages().send(vkCore.getActor()).peerId(peerId).message(msg).execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public MessagesSendQuery getSendQuery(){
        return vkCore.getVk().messages().send(vkCore.getActor());
    }

    /**
     * Обращается к VK API и получает объект, описывающий пользователя.
     * @param id идентификатор пользователя в VK
     * @return {@link UserXtrCounters} информацию о пользователе
     * @see UserXtrCounters
     */
    public UserXtrCounters getUserInfo(int id){
        try {
            return vkCore.getVk().users()
                    .get(vkCore.getActor())
                    .userIds(String.valueOf(id))
                    .execute()
                    .get(0);
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws ClientException, ApiException {
        VKManager vk = new VKManager();
        vk.getSendQuery().attachment(MenheraSprite.HIDDEN_SPRITE).chatId(-172998024)
                .randomId(LocaleMath.randInt(1200124120,124124312)).execute();
    }
}