package com.example.pfood.Classes;

public class Food {
    private String name;
    private Integer price;
    private String description;
    private Integer id;
    private String imageUrl;

    public Food() {

    }

    public Food(String name, Integer price, String description, Integer id, String imageUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return this.name;
    }

    public Integer getPrice() {
        return this.price;
    }

    public String getDescription() {
        return this.description;
    }

    public Integer getId() {
        return this.id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Food getFood() {
        return this;
    }
}
