package core.modules.notice;

/**
 * @author Arthur Kupriyanov
 */
public class NotificationNotFoundException extends Exception {
    NotificationNotFoundException(){
        super( "Объявление не найдено", new Exception(), true, false);
    }
}
