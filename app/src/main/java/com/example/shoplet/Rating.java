package com.example.shoplet;

public class Rating {
    private int rating;
    private String review;

    public Rating() {
        // Default constructor required for Firebase
    }

    public Rating(int rating, String review) {
        this.rating = rating;
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}

