package hieuntph22081.fpoly.goidi.model;

public class LoaiMonAn {
    private int id;
    private int img;
    private String ten;

    public LoaiMonAn() {
    }

    public LoaiMonAn(int id, int img, String ten) {
        this.id = id;
        this.img = img;
        this.ten = ten;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
