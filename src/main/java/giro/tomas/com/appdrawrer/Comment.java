package giro.tomas.com.appdrawrer;

public class Comment {
    private String text;
    private String date;
    private String postId;
    private String userName;

    public Comment(String text, String date, String postId, String userName) {
        this.text = text;
        this.date = date;
        this.postId = postId;
        this.userName = userName;
    }

    public String getPostId() {
        return postId;
    }

    public Comment() {
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getUserName() {
        return userName;
    }
}
