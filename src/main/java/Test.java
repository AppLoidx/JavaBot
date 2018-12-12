
import com.sun.istack.internal.Nullable;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


class Tests {
    public static void someFunc(@Nullable String a,String h,@Nullable int b){
        System.out.println(a);
        System.out.println(h);
        System.out.println(b);
    }
    public static void main(String[] args) throws NullPointerException, ClientException, ApiException, InterruptedException, IOException {

        someFunc("sadsd", null, 123);

    }
}

public class Test {
    public int a = 4;
    protected int b = 5;
    private int g = 3;

    protected static void func1(){

    }
    public static void funcpub(){

    }
}
