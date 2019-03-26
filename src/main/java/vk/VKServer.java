package vk;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ApiServerException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import core.commands.spam.EveningSpam;
import core.commands.spam.MorningSpam;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VKServer {
    public static VKCore vkCore;

    static {
        try {
            vkCore = new VKCore();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws NullPointerException, ApiException, InterruptedException {

        new Event().addCommand("07:00", new MorningSpam());
        new Event().addCommand("20:05", new EveningSpam());

        new Thread(new CustomEvent()).start();

        System.out.println("Running server...");
        while (true) {
            Thread.sleep(300);
            try {
                new Event().handlePerDay();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                Message message = vkCore.getMessage();
            if (message != null) {
                    ExecutorService exec = Executors.newCachedThreadPool();
                    exec.execute(new Messenger(vkCore.getActor(), vkCore.getVk(), message));
                }
            } catch (ClientException e) {
                System.out.println("Нет интернет соединения");
                final int RECONNECT_TIME = 10000;
                System.out.println("Повторное соединение через " + RECONNECT_TIME / 1000 + " секунд");
                Thread.sleep(RECONNECT_TIME);

            } catch (ApiServerException e){
                System.out.println("Ошибка API. Скорее всего из-за ts и max_msg_id");
                e.printStackTrace();
                System.out.println("Переопределяем VKCore...");
                try {
                    vkCore = new VKCore();
                } catch (ClientException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}