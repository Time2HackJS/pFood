package com.example.pfood.Classes;

import com.example.pfood.NetworkClasses.NetworkUsers;
import com.example.pfood.ResponseModels.PointModel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AppUsers implements NetworkUsers.GetUserInfoCallback{
    private static AppUsers instance;
    private AppUsers() {}

    public ArrayList<User> userList = new ArrayList<>();
    public ArrayList<Team> teamList = new ArrayList<>();
    public String uId;

    public User currentUser;

    public static synchronized AppUsers getInstance() {
        if (instance == null) {
            instance = new AppUsers();
        }

        return instance;
    }

    public void refreshPoints(String userId) {
        NetworkUsers.onGetUserInfoCallback(this);
        NetworkUsers.getUserInfo(userId);

        this.uId = userId;
    }

    @Override
    public void onResultCode(Integer resultCode) {

    }

    @Override
    public void onResult(PointModel result) {
        FirebaseDatabase.getInstance().getReference("users").child(this.uId)
                .child("rating").setValue(Integer.parseInt(result.point_inteam));
    }
}
