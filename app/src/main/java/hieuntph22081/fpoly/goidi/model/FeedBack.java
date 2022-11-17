package hieuntph22081.fpoly.goidi.model;

public class FeedBack {
    private String id;
    private String content;
    private User user;
    private String date;

    public FeedBack() {
    }

    public FeedBack(String id, String content, User user, String date) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
