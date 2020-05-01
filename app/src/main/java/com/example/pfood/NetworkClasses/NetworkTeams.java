package com.example.pfood.NetworkClasses;

import android.util.Log;

import com.example.pfood.Interfaces.TeamAPI;
import com.example.pfood.Interfaces.UserAPI;
import com.example.pfood.ResponseModels.PointModel;
import com.example.pfood.ResponseModels.ResponseModel;
import com.example.pfood.ResponseModels.TeamModel;
import com.example.pfood.ResponseModels.UserModel;
import com.example.pfood.ResponseModels.UserMonthModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkTeams {

    private static TeamAPI serviceAPI = TeamAPI.Factory.create();

    static GetTeamsCallback getTeamsCallback;
    static GetUserInTeamCallback getUserInTeamCallback;
    static AddTeamCallback addTeamCallback;

    public static void getTeams() {
        Call call = serviceAPI.getTeams();

        call.enqueue(new Callback<List<TeamModel>>() {
            @Override
            public void onResponse(Call<List<TeamModel>> call, Response<List<TeamModel>> response) {
                switch (response.code()) {
                    case 200: {
                        getTeamsCallback.onResult(response.body());
                        getTeamsCallback.onResultCode(response.code());
                    }
                    default: {
                        getTeamsCallback.onResultCode(response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                getTeamsCallback.onResultCode(500);

                Log.i("ERROR", t.getMessage());
            }
        });
    }

    public static void getUsersInTeam(String id) {
        Call call = serviceAPI.getUsersInTeam(id);

        call.enqueue(new Callback<UserModel[]>() {
            @Override
            public void onResponse(Call<UserModel[]> call, Response<UserModel[]> response) {
                switch (response.code()) {
                    case 200: {
                        getUserInTeamCallback.onResult(response.body());
                        getUserInTeamCallback.onResultCode(response.code());
                    }
                    default: {
                        getUserInTeamCallback.onResultCode(response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                getUserInTeamCallback.onResultCode(500);

                Log.i("ERROR", t.getMessage());
            }
        });
    }

    public static void addTeam(String name, String id_user, String invite) {
        Call call = serviceAPI.addTeam(name, id_user, invite);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                switch (response.code()) {
                    case 200: {
                        addTeamCallback.onResult(response.body());
                        addTeamCallback.onResultCode(response.code());
                    }
                    default: {
                        addTeamCallback.onResultCode(response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                addTeamCallback.onResultCode(500);

                Log.i("ERROR", t.getMessage());
            }
        });
    }

    public interface GetTeamsCallback {
        void onResultCode(Integer resultCode);
        void onResult(List<TeamModel> result);
    }

    public interface GetUserInTeamCallback {
        void onResultCode(Integer resultCode);
        void onResult(UserModel[] result);
    }

    public interface AddTeamCallback {
        void onResultCode(Integer resultCode);
        void onResult(ResponseModel result);
    }

    public static void onGetTeamsCallback(GetTeamsCallback callback) {
        getTeamsCallback = callback;
    }

    public static void onGetUserInTeamCallback(GetUserInTeamCallback callback) {
        getUserInTeamCallback = callback;
    }

    public static void onAddTeamCallback(AddTeamCallback callback) {
        addTeamCallback = callback;
    }
}
