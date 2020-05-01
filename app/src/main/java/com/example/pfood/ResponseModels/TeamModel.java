package com.example.pfood.ResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamModel {
    @SerializedName("id")
    @Expose
    public String count = null;

    @SerializedName("Name")
    @Expose
    public String name = null;

    @SerializedName("Point")
    @Expose
    public String point = null;
}
