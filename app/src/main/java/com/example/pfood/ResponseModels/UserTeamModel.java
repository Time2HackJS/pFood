package com.example.pfood.ResponseModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserTeamModel {
    @SerializedName("user_id_team")
    @Expose
    public String user_id_team = null;

    @SerializedName("point_inteam")
    @Expose
    public String point_inteam = null;
}
