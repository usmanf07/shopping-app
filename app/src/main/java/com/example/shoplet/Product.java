package com.example.shoplet;

import java.io.Serializable;

public class Product implements Serializable {
    private String name;
    private double ratings;
    private String price;
    private int imageResId;

    public Product(String name, double ratings, String price, int imageResId) {
        this.name = name;
        this.ratings = ratings;
        this.price = price;
        this.imageResId = imageResId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRatings() {
        return ratings;
    }

    public void setRatings(double description) {
        this.ratings = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
