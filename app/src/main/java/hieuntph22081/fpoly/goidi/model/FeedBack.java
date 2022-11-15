package hieuntph22081.fpoly.goidi.model;

public class FeedBack {
    private int id;
    private String content;
    private String userid;
    private int date;

    public FeedBack() {
    }

    public FeedBack(int id, String content, String userid, int date) {
        this.id = id;
        this.content = content;
        this.userid = userid;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}
