
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
import core.modules.TeachersNotesDB;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;


class Tests {
    public static void someFunc(@Nullable String a,String h,@Nullable int b){
        System.out.println(a);
        System.out.println(h);
        System.out.println(b);
    }
    public static void main(String[] args) throws NullPointerException, ClientException, ApiException, InterruptedException, IOException, SQLException, ClassNotFoundException {

        System.out.println("ggggt".split("b").length);
    }
}

public class Test {

    public int a = 4;
    protected int b = 5;
    private int g = 3;

    protected static void func1() throws Exception {

    throw new Exception(){
        public String Exception(){
            return "Ошибка";
        }
    };

    }
    public static void funcpub(){

    }
}
