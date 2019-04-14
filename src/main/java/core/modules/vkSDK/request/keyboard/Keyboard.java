package core.modules.vkSDK.request.keyboard;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arthur Kupriyanov
 */
public class Keyboard {
    @SerializedName("one_time")
    private boolean oneTime;
    @SerializedName("buttons")
    private ArrayList<ArrayList<VKButton>> buttons = new ArrayList<>();

    {
        buttons.add(new ArrayList<>());
        buttons.add(new ArrayList<>());
        buttons.add(new ArrayList<>());
        buttons.add(new ArrayList<>());
    }

    public void addButtons(ArrayList<VKButton> vkButtonArrayList, int row){
        try {
            if (row >= 0 && row <= 3) {
                ArrayList<ArrayList<VKButton>> newButtons = new ArrayList<>();
                for (int rowCount = 0; rowCount <= buttons.size(); rowCount++) {
                    if (rowCount != row) {
                        if (!buttons.get(rowCount).isEmpty()) newButtons.add(buttons.get(rowCount));
                    } else {
                        newButtons.add(vkButtonArrayList);
                        break;
                    }
                }
                buttons = newButtons;

            }
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public void setOneTime(boolean oneTime) {
        this.oneTime = oneTime;
    }

    public Keyboard setEmpty(){
        buttons = new ArrayList<>();
        return this;
    }
}
