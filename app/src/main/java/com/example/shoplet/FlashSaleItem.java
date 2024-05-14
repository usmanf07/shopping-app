package com.example.shoplet;

public class FlashSaleItem {
    private int imageResource;
    private String name;
    private String rating;
    private String price;

    public FlashSaleItem(int imageResource, String name, String rating, String price) {
        this.imageResource = imageResource;
        this.name = name;
        this.rating = rating;
        this.price = price;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
