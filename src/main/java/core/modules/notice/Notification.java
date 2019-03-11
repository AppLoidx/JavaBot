package core.modules.notice;

/**
 * @author Arthur Kupriyanov
 */
public class Notification {
    public final String message;
    public final int authorID;
    public final int ID;
    public final String date;

    public Notification(String message, int authorID, int ID, String date){
        this.message = message.trim();
        this.authorID = authorID;
        this.ID = ID;
        this.date = date.trim();
    }
}
