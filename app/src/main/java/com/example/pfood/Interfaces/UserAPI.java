package com.example.pfood.Interfaces;

import com.example.pfood.ResponseModels.PointModel;
import com.example.pfood.ResponseModels.ResponseModel;
import com.example.pfood.ResponseModels.UserModel;
import com.example.pfood.ResponseModels.UserMonthModel;
import com.example.pfood.ResponseModels.UserTeamModel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserAPI {
    @GET("add_user.php?key=gDjRtEYOTwqW0w@ezsVn")
    Call<ResponseModel> addUser(
            @Query("id") String id
    );

    @GET("getUserInfo.php?key=gDjRtEYOTwqW0w@ezsVn")
    Call<PointModel> getUserInfo(
            @Query("id") String id
    );

    @GET("add_point_user.php?key=gDjRtEYOTwqW0w@ezsVn")
    Call<ResponseModel> addPoints(
            @Query("id") String id,
            @Query("point") String point
    );

    @GET("get_top_user.php?key=gDjRtEYOTwqW0w@ezsVn")
    Call<UserModel[]> getTopUser();

    @GET("get_top_userm.php?key=gDjRtEYOTwqW0w@ezsVn")
    Call<UserMonthModel[]> getMonthTopUser();

    @GET("get_team_user.php?key=gDjRtEYOTwqW0w@ezsVn")
    Call<UserTeamModel> getUserTeam(
            @Query("id") String id
    );

    @GET("addUserToTeam.php?key=gDjRtEYOTwqW0w@ezsVn")
    Call<ResponseModel> addUserToTeam(
            @Query("id") String id,
            @Query("invite") String invite
    );

    class Factory {
        public static UserAPI create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://foodappbackend.000webhostapp.com/")
                    .build();

            return retrofit.create(UserAPI.class);
        }
    }
}
