package com.example.pfood.model;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamItem {
    public HashMap<String, String> teamMembers;
    public String teamName;
    public Long teamPlace;
    public Long teamRating;

    public TeamItem(){}

    public TeamItem(HashMap<String, String> teamMembers, String teamName, Long teamPlace, Long teamRating) {
        this.teamMembers = teamMembers;
        this.teamName = teamName;
        this.teamPlace = teamPlace;
        this.teamRating = teamRating;
    }
}
