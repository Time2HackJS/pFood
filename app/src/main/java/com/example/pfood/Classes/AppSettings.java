package com.example.pfood.Classes;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pfood.R;

import java.util.ArrayList;

public class AppSettings {
    private static AppSettings instance;
    private AppSettings() {}

    public static synchronized AppSettings getInstance() {
        if (instance == null) {
            instance = new AppSettings();
        }

        return instance;
    }

    public FoodCategory clickedCategory;
    public Food clickedFood;
    public ArrayList<FoodCategory> fcList = new ArrayList<>();
    public ArrayList<Food> foodList = new ArrayList<>();
    public ArrayList<Food> foodCache = new ArrayList<>();
    public ArrayList<FoodCollectable> foodCart = new ArrayList<>();
    public TextView tvNum;
    public ImageView ivCircle;
    public Integer fullNumPrice = 0;
    public Integer foodCount = 0;
    public TextView fullPrice;
    public ListView foodListView;

    public void fillFood() {
        if (foodList.isEmpty() && fcList.isEmpty()) {
            foodList.add(new Food("С лососем", 350, "Здесь должно находиться очень много-много текста, который якобы с трудом будет влезать на этом маленьком экране моего не менее маленького смартфона.", 1, R.drawable.sushi1));
            foodList.add(new Food("С авокадо", 250, "Рис и авокадо", 2, R.drawable.sushi2));
            foodList.add(new Food("Филадельфия", 400, "Рис и рыба какая-то", 3, R.drawable.sushi3));
            foodList.add(new Food("Калифорния", 300, "Рис и икра", 4, R.drawable.sushi4));

            fcList.add(new FoodCategory("Суши", 1, foodList, R.drawable.sushi));

            fcList.add(new FoodCategory("Роллы", 2, foodList, R.drawable.rolls));
            fcList.add(new FoodCategory("Лапша", 3, foodList, R.drawable.nooddles));
        }
    }

    public FoodCollectable findFood(Integer id) {
        for (FoodCollectable food : foodCart) {
            if (food.getId().equals(id)) {
                return food;
            }
        }
        return null;
    }
}
