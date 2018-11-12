
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.groups.GroupsGetLongPollServerQuery;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;
import core.Commander;

import java.util.List;

public class Test {
    public static void main(String[] args) throws NullPointerException, ClientException, ApiException, InterruptedException {
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        int APP_ID = -;
        int groupId = -;
        String CLIENT_SECRET = "-";
        String REDIRECT_URI = "";
        String access_token = "-";

        GroupActor actor = new GroupActor(groupId, access_token);

        vk.messages().send(actor).userId(255396611).message("ROFFFL");
        GroupsGetLongPollServerQuery response = vk.groups().getLongPollServer(actor);

        System.out.println("Server Started!");
        int ts = vk.messages().getLongPollServer(actor).execute().getTs();
        while (true) {
            Thread.sleep(300);
            MessagesGetLongPollHistoryQuery eventsQuery = vk.messages().getLongPollHistory(actor);
            eventsQuery.ts(ts);
            ts =  vk.messages().getLongPollServer(actor).execute().getTs();

            List<Message> messages = eventsQuery.execute().getMessages().getMessages();

            if (!messages.isEmpty() && !messages.get(0).isOut()){
                System.out.println(messages.get(0));
                vk.messages().send(actor).peerId(messages.get(0).getUserId()).message(Commander.getResponse(messages.get(0).getBody())).execute();
            }

        }

    }
}
