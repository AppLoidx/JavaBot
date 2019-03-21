package core.modules.config;

import com.google.gson.GsonBuilder;

/**
 * @author Arthur Kupriyanov
 */
public class Configuration {
    public static Settings getSettings(int vkid){
        String json = new ConfigDB().getSettings(vkid);
        return new GsonBuilder().create().fromJson(json, Settings.class);
    }

    public static void setSettings(Settings settings, int vkid){
        settings.setVkid(vkid).save();
    }
}
