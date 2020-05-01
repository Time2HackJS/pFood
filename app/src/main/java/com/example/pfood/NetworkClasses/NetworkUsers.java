package com.example.pfood.NetworkClasses;

import android.util.Log;

import com.example.pfood.Interfaces.UserAPI;
import com.example.pfood.ResponseModels.PointModel;
import com.example.pfood.ResponseModels.ResponseModel;
import com.example.pfood.ResponseModels.UserModel;
import com.example.pfood.ResponseModels.UserMonthModel;
import com.example.pfood.ResponseModels.UserTeamModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkUsers {

    private static UserAPI serviceAPI = UserAPI.Factory.create();

    static AddUserCallback addUserCallback;
    static GetUserInfoCallback getUserInfoCallback;
    static AddUserPointsCallback addUserPointsCallback;
    static GetTopUserCallback getTopUserCallback;
    static GetMonthTopUserCallback getMonthTopUserCallback;
    static GetUserTeamCallback getUserTeamCallback;
    static AddUserToTeamCallback addUserToTeamCallback;

    public static void addUser(String id) {
        Call call = serviceAPI.addUser(id);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                switch (response.code()) {
                    case 200: {
                        addUserCallback.onResult(response.body());
                        addUserCallback.onResultCode(response.code());
                        Log.i("CHECK RESPONSE", response.code() + "");
                    }
                    default: {
                        addUserCallback.onResultCode(response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                addUserCallback.onResultCode(500);

                Log.i("ERROR", t.getMessage());
            }
        });
    }

    public static void getUserInfo(String id) {
        Call call = serviceAPI.getUserInfo(id);

        call.enqueue(new Callback<PointModel>() {
            @Override
            public void onResponse(Call<PointModel> call, Response<PointModel> response) {
                switch (response.code()) {
                    case 200: {
                        getUserInfoCallback.onResult(response.body());
                        getUserInfoCallback.onResultCode(response.code());
                    }
                    default: {
                        getUserInfoCallback.onResultCode(response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                getUserInfoCallback.onResultCode(500);

                Log.i("ERROR", t.getMessage());
            }
        });
    }

    public static void addUserPoints(String id, String points) {
        Call call = serviceAPI.addPoints(id, points);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                switch (response.code()) {
                    case 200: {
                        addUserPointsCallback.onResult(response.body());
                        addUserPointsCallback.onResultCode(response.code());
                    }
                    default: {
                        addUserPointsCallback.onResultCode(response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                addUserPointsCallback.onResultCode(500);

                Log.i("ERROR", t.getMessage());
            }
        });
    }

    public static void getTopUser() {
        Call call = serviceAPI.getTopUser();

        call.enqueue(new Callback<UserModel[]>() {
            @Override
            public void onResponse(Call<UserModel[]> call, Response<UserModel[]> response) {
                switch (response.code()) {
                    case 200: {
                        getTopUserCallback.onResult(response.body());
                        getTopUserCallback.onResultCode(response.code());
                    }
                    default: {
                        getTopUserCallback.onResultCode(response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                getTopUserCallback.onResultCode(500);

                Log.i("ERROR", t.getMessage());
            }
        });
    }

    public static void getMonthTopUser() {
        Call call = serviceAPI.getMonthTopUser();

        call.enqueue(new Callback<UserModel[]>() {
            @Override
            public void onResponse(Call<UserModel[]> call, Response<UserModel[]> response) {
                switch (response.code()) {
                    case 200: {
                        getMonthTopUserCallback.onResult(response.body());
                        getMonthTopUserCallback.onResultCode(response.code());
                    }
                    default: {
                        getMonthTopUserCallback.onResultCode(response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                getMonthTopUserCallback.onResultCode(500);

                Log.i("ERROR", t.getMessage());
            }
        });
    }

    public static void getUserTeam(String id) {
        Call call = serviceAPI.getUserTeam(id);

        call.enqueue(new Callback<UserTeamModel>() {
            @Override
            public void onResponse(Call<UserTeamModel> call, Response<UserTeamModel> response) {
                Log.i("RESPONSE CODE 1", "CHECK");
                switch (response.code()) {
                    case 200: {
                        getUserTeamCallback.onResult(response.body());
                        getUserTeamCallback.onResultCode(response.code());
                    }
                    default: {
                        getUserTeamCallback.onResultCode(response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                getUserTeamCallback.onResultCode(500);

                Log.i("ERROR", t.getMessage());
            }
        });
    }

    public static void addUserToTeam(String id, String invite) {
        Call call = serviceAPI.addUserToTeam(id, invite);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Log.i("RESPONSE CODE EXTENDED", response.code() + "");
                switch (response.code()) {
                    case 200: {
                        addUserToTeamCallback.onAddResult(response.body());
                        addUserToTeamCallback.onResultCode(response.code());
                    }
                    default: {
                        addUserToTeamCallback.onResultCode(response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                addUserToTeamCallback.onResultCode(500);

                Log.i("ERROR", t.getMessage());
            }
        });
    }

    public interface AddUserCallback {
        void onResultCode(Integer resultCode);
        void onResult(ResponseModel result);
    }

    public interface GetUserInfoCallback {
        void onResultCode(Integer resultCode);
        void onResult(PointModel result);
    }

    public interface AddUserPointsCallback {
        void onResultCode(Integer resultCode);
        void onResult(ResponseModel result);
    }

    public interface GetTopUserCallback {
        void onResultCode(Integer resultCode);
        void onResult(UserModel[] result);
    }

    public interface GetMonthTopUserCallback {
        void onResultCode(Integer resultCode);
        void onResult(UserModel[] result);
    }

    public interface GetUserTeamCallback {
        void onResultCode(Integer resultCode);
        void onResult(UserTeamModel result);
    }

    public interface AddUserToTeamCallback {
        void onResultCode(Integer resultCode);
        void onAddResult(ResponseModel result);
    }

    public static void onAddUserCallback(AddUserCallback callback) {
        addUserCallback = callback;
    }

    public static void onGetUserInfoCallback(GetUserInfoCallback callback) {
        getUserInfoCallback = callback;
    }

    public static void onAddUserPointsCallback(AddUserPointsCallback callback) {
        addUserPointsCallback = callback;
    }

    public static void onGetTopUserCallback(GetTopUserCallback callback) {
        getTopUserCallback = callback;
    }

    public static void onGetMonthTopUserCallback (GetMonthTopUserCallback callback) {
        getMonthTopUserCallback = callback;
    }

    public static void onGetUserTeamCallback (GetUserTeamCallback callback) {
        getUserTeamCallback = callback;
    }

    public static void onAddUserToTeamCallback (AddUserToTeamCallback callback) {
        addUserToTeamCallback = callback;
    }
}
