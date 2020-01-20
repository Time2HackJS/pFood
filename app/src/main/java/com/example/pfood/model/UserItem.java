package com.example.pfood.model;

public class UserItem {
    public String name;
    public String address;
    public String inviteCode;
    public Long bonusesCount;
    public Long rating;
    public Long ratingPosition;

    public UserItem() {
    }

    public UserItem(
            String name,
            String address,
            String inviteCode,
            Long bonusesCount,
            Long rating,
            Long ratingPosition) {
        this.name = name;
        this.address = address;
        this.inviteCode = inviteCode;
        this.bonusesCount = bonusesCount;
        this.rating = rating;
        this.ratingPosition = ratingPosition;
    }
}
