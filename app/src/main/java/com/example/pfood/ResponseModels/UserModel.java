package com.example.pfood.ResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("id")
    @Expose
    String count = null;

    @SerializedName("point")
    @Expose
    String point = null;


}
