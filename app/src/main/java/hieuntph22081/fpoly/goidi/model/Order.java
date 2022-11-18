package hieuntph22081.fpoly.goidi.model;

import java.util.List;

public class Order {
    private String id;
    private User user;
    private Table table;
    private List<OrderDish> dishes;
    private String date;
    private String startTime;
    private String endTime;
    private double total;
    private int status;

    public Order() {
    }

    public Order(String id, User user, Table table, List<OrderDish> dishes, String date, String startTime, String endTime, double total, int status) {
        this.id = id;
        this.user = user;
        this.table = table;
        this.dishes = dishes;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.total = total;
        this.status = status;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public List<OrderDish> getDishes() {
        return dishes;
    }

    public void setDishes(List<OrderDish> dishes) {
        this.dishes = dishes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal() {
        double temp = 0;
        for (OrderDish orderDish : dishes) {
            temp = temp + (orderDish.getDish().getGia()*orderDish.getQuantity());
        }
        this.total = temp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
