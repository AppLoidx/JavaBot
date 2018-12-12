package vk;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class VKCore {
    private VkApiClient vk;
    private int ts;
    private GroupActor actor;
    public VKCore() throws IOException, ClientException, ApiException {

        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);

        Properties prop = new Properties();
        prop.load(new FileInputStream("src/main/resources/vkconfig.properties"));

        int groupId = Integer.valueOf(prop.getProperty("groupId"));
        String access_token = prop.getProperty("accessToken");

        actor = new GroupActor(groupId, access_token);

        System.out.println("Server Started!");

        ts = vk.messages().getLongPollServer(actor).execute().getTs();
    }

    public GroupActor getActor() {
        return actor;
    }

    public VkApiClient getVk() {
        return vk;
    }

    public String[] getMessage(){
        MessagesGetLongPollHistoryQuery eventsQuery = vk.messages().getLongPollHistory(actor);
        eventsQuery.ts(ts);
        try {
            ts =  vk.messages().getLongPollServer(actor).execute().getTs();

        List<Message> messages = eventsQuery.execute().getMessages().getMessages();
        if (!messages.isEmpty() && !messages.get(0).isOut()) {
            return new String[]{messages.get(0).getBody(),String.valueOf(messages.get(0).getUserId())};
        }

        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
        return new String[]{"Error"};
    }
}
