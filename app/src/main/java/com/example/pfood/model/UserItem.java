package com.example.pfood.model;

public class UserItem {
    public String name;
    public String address;
    public String inviteCode;
    public Long bonusesCount;

    public UserItem() {
    }

    public UserItem(String name, String address, String inviteCode, Long bonusesCount) {
        this.name = name;
        this.address = address;
        this.inviteCode = inviteCode;
        this.bonusesCount = bonusesCount;
    }
}
