package core.modules.config;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * @author Arthur Kupriyanov
 */
public class Settings {
    private static ConfigDB db = new ConfigDB();
    @SerializedName("morning_spam")
    private boolean morningSpam;
    @SerializedName("evening_spam")
    private boolean eveningSpam;
    @SerializedName("app_access")
    private boolean appAccess;
    @SerializedName("images")
    private boolean images;
    private transient int vkid;

    public boolean isMorningSpam() {
        return morningSpam;
    }

    public void setMorningSpam(boolean morningSpam) {
        this.morningSpam = morningSpam;
    }

    public boolean isEveningSpam() {
        return eveningSpam;
    }

    public void setEveningSpam(boolean eveningSpam) {
        this.eveningSpam = eveningSpam;
    }

    public boolean isAppAccess() {
        return appAccess;
    }

    public void setAppAccess(boolean appAccess) {
        this.appAccess = appAccess;
    }

    public boolean isImages() {
        return images;
    }

    public void setImages(boolean images) {
        this.images = images;
    }

    public boolean save(){
        String json = new GsonBuilder().create().toJson(this);
        if (vkid!=0) return db.saveSettings(vkid, json);
        else return false;

    }

    public Settings setVkid(int vkid) {
        this.vkid = vkid;
        return this;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "morningSpam=" + morningSpam +
                ", eveningSpam=" + eveningSpam +
                ", appAccess=" + appAccess +
                ", images=" + images +
                ", vkid=" + vkid +
                '}';
    }
}
