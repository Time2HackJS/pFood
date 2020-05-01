package com.example.pfood.Interfaces;

import com.example.pfood.ResponseModels.ResponseModel;
import com.example.pfood.ResponseModels.TeamModel;
import com.example.pfood.ResponseModels.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TeamAPI {
    @GET("get_top_team.php?key=gDjRtEYOTwqW0w@ezsVn")
    Call<List<TeamModel>> getTeams();

    @GET("get_user_in_team?key=gDjRtEYOTwqW0w@ezsVn")
    Call<UserModel[]> getUsersInTeam(
            @Query("id_team") String id_team
    );

    @GET("add_team.php?key=gDjRtEYOTwqW0w@ezsVn")
    Call<ResponseModel> addTeam(
            @Query("name") String name,
            @Query("id_user") String id_user,
            @Query("invite") String invite
    );

    class Factory {
        public static TeamAPI create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://foodappbackend.000webhostapp.com/")
                    .build();

            return retrofit.create(TeamAPI.class);
        }
    }
}
