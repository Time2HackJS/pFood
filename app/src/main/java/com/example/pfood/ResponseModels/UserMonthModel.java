package com.example.pfood.ResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserMonthModel {
    @SerializedName("id")
    @Expose
    String count = null;

    @SerializedName("point_m")
    @Expose
    String point_m = null;
}
