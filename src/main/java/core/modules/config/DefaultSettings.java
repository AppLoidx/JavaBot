package core.modules.config;

/**
 * @author Arthur Kupriyanov
 */
public class DefaultSettings {
    public Settings set(Settings settings){
        settings.setAppAccess(true);
        settings.setEveningSpam(true);
        settings.setMorningSpam(false);
        settings.setImages(true);

        return settings;
    }
}
