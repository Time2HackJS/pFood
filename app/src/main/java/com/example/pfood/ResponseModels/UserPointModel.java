package com.example.pfood.ResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPointModel {
    @SerializedName("id")
    @Expose
    String count = null;

    @SerializedName("point")
    @Expose
    String point = null;

    @SerializedName("place")
    @Expose
    String place = null;

    @SerializedName("place_m")
    @Expose
    String place_m = null;
}
