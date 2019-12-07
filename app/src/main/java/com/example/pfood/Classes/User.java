package com.example.pfood.Classes;

public class User {
    private String name;
    private Integer points;
    private Integer top_tier;

    public User(Integer top_tier, String name, Integer points) {
        this.name = name;
        this.points = points;
        this.top_tier = top_tier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getTopTier() {
        return top_tier;
    }

    public void setTopTier(Integer top_tier) {
        this.top_tier = top_tier;
    }
}
