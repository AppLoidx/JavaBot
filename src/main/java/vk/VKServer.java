package vk;


import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserXtrCounters;

import java.io.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VKServer {
    public static VKCore vkCore;

    static {
        try {
            vkCore = new VKCore();
        } catch (IOException | ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws NullPointerException, ApiException, InterruptedException, IOException {

        while (true) {
            Thread.sleep(300);
            try {
                String[] message = vkCore.getMessage();
                if (!message[0].equals("Error")) {
                    UserXtrCounters userInfo = vkCore.getUserInfo(message[1]);
                    ExecutorService exec = Executors.newCachedThreadPool();
                    exec.execute(new Messenger(vkCore.getActor(), vkCore.getVk(), message[0], Integer.valueOf(message[1]), userInfo));
                }
            } catch (ClientException e) {
                System.out.println("Нет интернет соединения");
                final int RECONNECT_TIME = 10000;
                System.out.println("Повторное соединение через " + RECONNECT_TIME / 1000 + " секунд");
                Thread.sleep(RECONNECT_TIME);

            }
        }
    }
}