package core.modules.notice;

/**
 * @author Arthur Kupriyanov
 */
public class Notification {
    final String message;
    final int authorID;
    final int ID;
    final String date;

    public Notification(String message, int authorID, int ID, String date){
        this.message = message.trim();
        this.authorID = authorID;
        this.ID = ID;
        this.date = date.trim();
    }
}
