package hieuntph22081.fpoly.goidi.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    private int maTV, phone;
    private String hoTen;
    private String namSinh;

    public User() {
    }

    public int getMaTV() {
        return maTV;
    }

    public void setMaTV(int maTV) {
        this.maTV = maTV;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(String namSinh) {
        this.namSinh = namSinh;
    }

    public User(int maTV, int phone, String hoTen, String namSinh) {
        this.maTV = maTV;
        this.phone = phone;
        this.hoTen = hoTen;
        this.namSinh = namSinh;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("hoten", hoTen);
        return result;
    }
}
