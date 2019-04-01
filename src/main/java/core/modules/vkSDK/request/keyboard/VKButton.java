package core.modules.vkSDK.request.keyboard;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arthur Kupriyanov
 */
public class VKButton {
    @SerializedName("action")
    private Map<String, Object> action = new HashMap<>();
    @SerializedName("color")
    private String color = KeyboardColor.POSITIVE.getValue();


    {
        action.put("type", "text");
    }
    public VKButton setLabel(String label){
        action.put("label", label);
        return this;
    }
    public VKButton setColor(KeyboardColor color){
        this.color = color.getValue();
        return this;
    }

}
