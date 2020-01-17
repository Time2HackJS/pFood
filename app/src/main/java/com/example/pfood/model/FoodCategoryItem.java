package com.example.pfood.model;

import androidx.annotation.NonNull;

public class FoodCategoryItem {
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @NonNull
    @Override
    public String toString() {
        return categoryName;
    }
}
