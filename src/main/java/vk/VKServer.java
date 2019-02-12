package vk;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.users.UserCounters;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VKServer {
    public static VKCore  vkCore;

    static {
        try {
            vkCore = new VKCore();
        } catch (IOException | ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws NullPointerException, ClientException, ApiException, InterruptedException, IOException {

        while (true) {
            Thread.sleep(300);
            String[] message = vkCore.getMessage();
            if (!message[0].equals("Error")) {
                UserXtrCounters userInfo = vkCore.getUserInfo(message[1]);
                ExecutorService exec = Executors.newCachedThreadPool();
                exec.execute(new Messenger(vkCore.getActor(), vkCore.getVk(), message[0], Integer.valueOf(message[1]), userInfo));
            }

        }

    }
}
