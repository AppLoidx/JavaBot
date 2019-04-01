package core.commands;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import core.commands.VKCommands.VKCommand;
import core.modules.vkSDK.request.KeyboardPostRequest;
import core.modules.vkSDK.request.keyboard.KeyboardColor;
import core.modules.vkSDK.request.keyboard.VKButton;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Arthur Kupriyanov
 */
public class Keyboard extends Command implements VKCommand {
    @Override
    protected void setConfig() {
        commandName = "keyboard";
    }

    @Override
    public String exec(Message message) {
        String[] keyboardsList = message.getBody().split(" ");
        if (keyboardsList.length <= 1){
            return "неверный формат команды. Введите заново";
        }
        if (keyboardsList[1].equals("reset")){
            try {
                new KeyboardPostRequest().sendKeyboard(new core.modules.vkSDK.request.keyboard.Keyboard().setEmpty(), message.getUserId());
            } catch (IOException | ApiException | ClientException e) {
                e.printStackTrace();
                return "Ошибка. " + e.getMessage();
            }
            return "";
        }
        if (keyboardsList.length > 13) return "слшиком много клавиш";

        ArrayList<VKButton> firstRow = new ArrayList<>();
        ArrayList<VKButton> secondRow = new ArrayList<>();
        ArrayList<VKButton> thirdRow = new ArrayList<>();
        ArrayList<VKButton> fourthRow = new ArrayList<>();
        int row = 0;
        for (int i = 1; i < keyboardsList.length; i++){
                VKButton button = new VKButton();
                if (keyboardsList[i].matches(".+\\..+")){
                    button.setLabel(keyboardsList[i].split("\\.")[0]);
                    button.setColor(KeyboardColor.getEnum(keyboardsList[i].split("\\.")[1]));
                } else {
                    button.setLabel(keyboardsList[i]);
                }
                switch (row){
                    case 0:
                        firstRow.add(button);
                        break;
                    case 1:
                        secondRow.add(button);
                        break;
                    case 2:
                        thirdRow.add(button);
                        break;
                    case 3:
                        if (keyboardsList.length==5) thirdRow.add(button);
                        else fourthRow.add(button);
                        break;
                }

                row++;
                if (row==4) row = 0;
            }

        core.modules.vkSDK.request.keyboard.Keyboard keyboard = new core.modules.vkSDK.request.keyboard.Keyboard();

        if (!firstRow.isEmpty()) keyboard.addButtons(firstRow, 0);
        if (!secondRow.isEmpty()) keyboard.addButtons(secondRow, 1);
        if (!thirdRow.isEmpty()) keyboard.addButtons(thirdRow, 2);
        if (!fourthRow.isEmpty()) keyboard.addButtons(fourthRow, 3);

        try {
            new KeyboardPostRequest().sendKeyboard(keyboard, message.getUserId());
        } catch (IOException | ApiException | ClientException e) {
            e.printStackTrace();
            return "Ошибка. " + e.getMessage();
        }
        return "";
    }
}
