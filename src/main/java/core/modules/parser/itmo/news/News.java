package core.modules.parser.itmo.news;

/**
 * @author Arthur Kupriyanov
 */
public class News {
    private String imageUrl;
    private String message;
    private String url;
    private String date;

    public News(String message, String url){
        this.message = message;
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public News setImageUrl(String url){
        imageUrl = url;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public News setDate(String date) {
        this.date = date;
        return this;
    }

    @Override
    public String toString() {
        return "message : \"" + message + "\"\n" +
                "image url \": " + imageUrl + "\"\n" +
                "date : \"" + date + "\"\n" +
                "url : " + url;
    }
}
