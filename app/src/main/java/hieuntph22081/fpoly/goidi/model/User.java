package hieuntph22081.fpoly.goidi.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    private String id, name, phone;
    private int role;
    private int soLan;



    public User() {
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("phone", phone);
        result.put("role", role);
        return result;
    }

    public User(String id, String name, String phone, int role) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

    public User(String id, String name, String phone, int role, int soLan) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.soLan = soLan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getSoLan() {
        return soLan;
    }

    public void setSoLan(int soLan) {
        this.soLan = soLan;
    }
}
