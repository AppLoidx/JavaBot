package core.modules.vkSDK;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import core.common.YesNoAction;
import core.modules.AskYesNo;
import core.modules.vkSDK.request.KeyboardPostRequest;
import core.modules.vkSDK.request.keyboard.Keyboard;
import core.modules.vkSDK.request.keyboard.KeyboardColor;
import core.modules.vkSDK.request.keyboard.VKButton;
import vk.VKManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class AskYesNoVK {
    public static void addAnswer(int id, boolean isYes){
        AskYesNo.addAnswer(String.valueOf(id), isYes);
        try {
            KeyboardPostRequest.sendKeyboard(Keyboard.getEmpty(), 0 );
        } catch (IOException | ClientException | ApiException e) {
            e.printStackTrace();
        }
    }
    public static void answer(int id, YesNoAction action){
        ArrayList<VKButton> vkButtons = new ArrayList<>();
        vkButtons.add(new VKButton().setLabel("Да").setColor(KeyboardColor.POSITIVE));
        vkButtons.add(new VKButton().setLabel("Нет").setColor(KeyboardColor.NEGATIVE));
        Keyboard keyboard = new Keyboard();
        keyboard.addButtons(vkButtons, 0);
        try {
            KeyboardPostRequest.sendKeyboard(keyboard, id);
        } catch (IOException | ClientException | ApiException e) {
            e.printStackTrace();
        }
        AskYesNo.answer(String.valueOf(id), action);

    }
    public static boolean isAnswered(int id){
        return AskYesNo.isAnswered(String.valueOf(id));
    }
}
