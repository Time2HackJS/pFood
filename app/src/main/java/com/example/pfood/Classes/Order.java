package com.example.pfood.Classes;

import java.util.ArrayList;

public class Order {
    private String userId;
    private String name;
    private String address;
    private String phone;
    private String time;
    private ArrayList<String> foodCart;
    private String commentary;
    private Integer price;
    private String paymentType;
    private String key;

    public Order(String userId, String name, String address, String phone, String time, ArrayList<String> foodCart, String commentary, Integer price, String paymentType) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.time = time;
        this.foodCart = foodCart;
        this.commentary = commentary;
        this.price = price;
        this.paymentType = paymentType;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTime() {
        return time;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<String> getFoodCart() {
        return foodCart;
    }

    public void setFoodCart(ArrayList<String> foodCart) {
        this.foodCart = foodCart;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
