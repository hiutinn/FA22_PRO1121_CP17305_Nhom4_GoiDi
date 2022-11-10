package hieuntph22081.fpoly.goidi.model;

public class MonAn {
    private int id;
    private String ten;
    private double gia;
    private int img;
    private int soLuong;

    public MonAn() {
    }

    public MonAn(int id, String ten, double gia, int img) {
        this.id = id;
        this.ten = ten;
        this.gia = gia;
        this.img = img;
    }

    public MonAn(int id, String ten, double gia, int img, int soLuong) {
        this.id = id;
        this.ten = ten;
        this.gia = gia;
        this.img = img;
        this.soLuong = soLuong;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}
