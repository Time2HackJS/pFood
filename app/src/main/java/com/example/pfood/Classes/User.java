package com.example.pfood.Classes;

public class User {
    private String name;
    private String address;
    private String inviteCode;
    private Integer bonusesCount;
    private Integer rating;
    private Integer ratingPosition;
    private Integer monthRating;
    private Integer monthRatingPosition;

    public User(String name, String address, String inviteCode, Integer bonusesCount, Integer rating, Integer ratingPosition, Integer monthRating, Integer monthRatingPosition) {
        this.name = name;
        this.address = address;
        this.inviteCode = inviteCode;
        this.bonusesCount = bonusesCount;
        this.rating = rating;
        this.ratingPosition = ratingPosition;
        this.monthRating = monthRating;
        this.monthRatingPosition = monthRatingPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Integer getBonusesCount() {
        return bonusesCount;
    }

    public void setBonusesCount(Integer bonusesCount) {
        this.bonusesCount = bonusesCount;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRatingPosition() {
        return ratingPosition;
    }

    public void setRatingPosition(Integer ratingPosition) {
        this.ratingPosition = ratingPosition;
    }

    public Integer getMonthRating() {
        return monthRating;
    }

    public void setMonthRating(Integer monthRating) {
        this.monthRating = monthRating;
    }

    public Integer getMonthRatingPosition() {
        return monthRatingPosition;
    }

    public void setMonthRatingPosition(Integer monthRatingPosition) {
        this.monthRatingPosition = monthRatingPosition;
    }
}
