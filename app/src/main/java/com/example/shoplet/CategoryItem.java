package com.example.shoplet;

public class CategoryItem {
    private String id;
    private String name;

    private String image;


    // Default constructor
    public CategoryItem() {
        // Default values or initialization can be done here
    }

    public CategoryItem(String categoryId, String name, String img) {
        this.id = categoryId;
        this.name = name;
        this.image = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
