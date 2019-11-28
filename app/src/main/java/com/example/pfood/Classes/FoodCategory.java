package com.example.pfood.Classes;

import com.example.pfood.Classes.Food;
import com.example.pfood.R;

import java.util.ArrayList;

public class FoodCategory {

    private String name;
    private Integer id;
    private ArrayList<Food> foodList;
    private Integer imageSource;

    public FoodCategory() {
    }

    public FoodCategory(String name, Integer id, ArrayList<Food> foodList, Integer imageSource) {
        this.name = name;
        this.id = id;

        this.foodList = new ArrayList<>();
        this.foodList = foodList;

        this.imageSource = imageSource;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFoodList(ArrayList<Food> foodList) {
        this.foodList = foodList;
    }

    public void setImageSource(Integer imageSource) {
        this.imageSource = imageSource;
    }


    public String getName() {
        return this.name;
    }

    public Integer getId() {
        return this.id;
    }

    public ArrayList<Food> getFoodList() {
        return this.foodList;
    }

    public Integer getImageSource() {
        return this.imageSource;
    }
}
