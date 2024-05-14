package com.example.shoplet;

public class CategoryItem {
    private String categoryId;
    private String name;

    // Default constructor
    public CategoryItem() {
        // Default values or initialization can be done here
        this.categoryId = "";
        this.name = "";
    }

    public CategoryItem(String categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
