package com.example.shoplet;

import java.util.Map;
import java.io.Serializable;
public class Product implements Serializable{
    private static final long serialVersionUID = 1L;
    private String description;

    private String image;
    private boolean freedelivery;
    private String fullname;
    private String name;
    private boolean onsale;
    private int price;
    private Map<String, Rating> ratings;
    private int sales;
    private int stock;
    private String categoryID;
    private double totalrating;

    public Product() {
        // Default constructor required for Firebase
    }

    public Product(String image, String description, boolean freedelivery, String fullname, String name, boolean onsale,
                   int price, Map<String, Rating> ratings, int sales, int stock, String categoryID, double totalrating) {
        this.image = image;
        this.description = description;
        this.freedelivery = freedelivery;
        this.fullname = fullname;
        this.name = name;
        this.onsale = onsale;
        this.price = price;
        this.ratings = ratings;
        this.sales = sales;
        this.stock = stock;
        this.categoryID = categoryID;
        this.totalrating = totalrating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return this.image;
    }

    public boolean isFreedelivery() {
        return freedelivery;
    }

    public void setFreedelivery(boolean freedelivery) {
        this.freedelivery = freedelivery;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnsale() {
        return onsale;
    }

    public void setOnsale(boolean onsale) {
        this.onsale = onsale;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Map<String, Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Map<String, Rating> ratings) {
        this.ratings = ratings;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public double getTotalrating() {
        return totalrating;
    }

    public void setTotalrating(double totalrating) {
        this.totalrating = totalrating;
    }
}
