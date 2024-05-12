package com.example.shoplet;

public class CartItem {
    private int imageResource;
    private String name;
    private double price;
    private int quantity;

    public CartItem(int imageResource, String name, double price, int quantity) {
        this.imageResource = imageResource;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
