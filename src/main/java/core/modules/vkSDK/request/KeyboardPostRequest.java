package core.modules.vkSDK.request;

import com.google.gson.GsonBuilder;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import core.common.LocaleMath;
import core.modules.vkSDK.request.keyboard.Keyboard;
import core.modules.vkSDK.request.keyboard.KeyboardColor;
import core.modules.vkSDK.request.keyboard.VKButton;
import vk.VKCore;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * @author Arthur Kupriyanov
 */
public class KeyboardPostRequest {
    private final static String BASE_URL = "https://api.vk.com/method/messages.send";

    public static void main(String[] args) throws IOException, ClientException, ApiException {
        Keyboard keyboard = new Keyboard();
        ArrayList<VKButton> firstRow = new ArrayList<>();
        ArrayList<VKButton> secondRow = new ArrayList<>();

        firstRow.add(new VKButton().setColor(KeyboardColor.POSITIVE).setLabel("tesss"));
        firstRow.add(new VKButton().setColor(KeyboardColor.POSITIVE).setLabel("tesss"));
        secondRow.add(new VKButton().setColor(KeyboardColor.NEGATIVE).setLabel("lol"));

        keyboard.addButtons(firstRow, 0);
        keyboard.addButtons(secondRow, 1);

        sendKeyboard(keyboard, 255396611);
    }

    public static void sendKeyboard(Keyboard keyboard, int userId) throws IOException, ClientException, ApiException {
        if (keyboard==null) keyboard = Keyboard.getEmpty();
        String jsonKeyboard = new GsonBuilder().create().toJson(keyboard);
        String urle = BASE_URL + "?" + "keyboard=" +
                jsonKeyboard + "&peer_id="
                + userId + "&message=Установка%20клавиатуры..." +
                "&random_id=" + LocaleMath.randInt(1000, 32000) +
                "&access_token="+ new VKCore().getActor().getAccessToken() + "&v=5.92";
        System.out.println(urle);
        URL url = new URL(urle);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent","");
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-length", "0");
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setDoInput(true);
        connection.connect();
        connection.getInputStream();
    }
}
