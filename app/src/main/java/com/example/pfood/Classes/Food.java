package com.example.pfood.Classes;

public class Food {
    private String name;
    private Integer price;
    private String description;
    private Integer id;
    private Integer imageSource;

    public Food() {

    }

    public Food(String name, Integer price, String description, Integer id, Integer imageSource) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.id = id;
        this.imageSource = imageSource;
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

    public void setImageSource(Integer imageSource) {
        this.imageSource = imageSource;
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

    public Integer getImageSource() {
        return this.imageSource;
    }
}
