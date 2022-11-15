package hieuntph22081.fpoly.goidi.model;

public class Dish {
    private String id;
    private String ten;
    private String img;
    private int soLuong;
    private double gia;

    public Dish() {
    }

    public Dish(String id, String ten, String img, int soLuong, double gia) {
        this.id = id;
        this.ten = ten;
        this.img = img;
        this.soLuong = soLuong;
        this.gia = gia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }
}
