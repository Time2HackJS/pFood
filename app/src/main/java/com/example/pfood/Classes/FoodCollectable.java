package com.example.pfood.Classes;

public class FoodCollectable extends Food {
    private Integer foodCount;
    private Integer fullPrice;

    public FoodCollectable() {
    }

    public FoodCollectable(Food food, Integer foodCount) {
        this.setName(food.getName());
        this.setPrice(food.getPrice());
        this.setDescription(food.getDescription());
        this.setId(food.getId());
        this.setImageSource(food.getImageSource());
        this.foodCount = foodCount;
        this.fullPrice = food.getPrice();
    }

    public void setFoodCount(Integer foodCount) {
        this.foodCount = foodCount;
        this.fullPrice = this.foodCount * this.getPrice();
    }

    public Integer getFoodCount() {
        return this.foodCount;
    }

    public Integer getFullPrice() {
        return this.fullPrice;
    }
}
