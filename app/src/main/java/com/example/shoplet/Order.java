package com.example.shoplet;

import java.util.HashMap;
import java.util.Map;

public class Order {
    private String id;
    private String paymentMethod;
    private int total;
    private String status;
    private String userId;
    private Map<String, Product> products;
    private String date;
    private String time;

    public Order() {
        // Default constructor required for calls to DataSnapshot.getValue(Order.class)
    }

    public Order(String id, String paymentMethod, int total, String status, String userId, Map<String, Product> products, String date, String time) {
        this.id = id;
        this.paymentMethod = paymentMethod;
        this.total = total;
        this.status = status;
        this.userId = userId;
        this.products = products;
        this.date = date;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getPaymentMethod() { return paymentMethod; }
    public int getTotal() { return total; }
    public String getStatus() { return status; }
    public String getUserId() { return userId; }
    public Map<String, Product> getProducts() { return products; }

    public void setId(String id) { this.id = id; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setTotal(int total) { this.total = total; }
    public void setStatus(String status) { this.status = status; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setProducts(Map<String, Product> products) { this.products = products; }
}

