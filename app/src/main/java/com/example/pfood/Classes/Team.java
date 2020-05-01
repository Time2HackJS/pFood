package com.example.pfood.Classes;

import java.util.ArrayList;

public class Team {
    private String name;
    private Integer teamPlace;
    private Integer teamRating;

    public Team(String name, Integer teamPlace, Integer teamRating) {
        this.name = name;
        this.teamPlace = teamPlace;
        this.teamRating = teamRating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTeamPlace() {
        return teamPlace;
    }

    public void setTeamPlace(Integer teamPlace) {
        this.teamPlace = teamPlace;
    }

    public Integer getTeamRating() {
        return teamRating;
    }

    public void setTeamRating(Integer teamRating) {
        this.teamRating = teamRating;
    }
}
