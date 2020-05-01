package com.example.pfood.NetworkClasses;

import android.util.Log;

import com.example.pfood.Classes.Order;
import com.example.pfood.Interfaces.OrderAPI;
import com.example.pfood.ResponseModels.ResponseModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkOrders {

    private static OrderAPI serviceAPI = OrderAPI.Factory.create();

    static AddOrderToHistoryCallback addOrderToHistoryCallback;

    public static void addOrderToHistory(Order order, String order_p, String complete_time, String status) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", order.getName())
                .addFormDataPart("address", order.getAddress())
                .addFormDataPart("phone", order.getPhone())
                .addFormDataPart("price", order.getPrice().toString())
                .addFormDataPart("time", order.getTime())
                .addFormDataPart("payment_type", order.getPaymentType())
                .addFormDataPart("order_p", order_p)
                .addFormDataPart("complete_time", complete_time)
                .addFormDataPart("status", status)
                .addFormDataPart("key", "gDjRtEYOTwqW0w@ezsVn")
                .build();

        Call call = serviceAPI.addOrderToHistory(requestBody);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                switch (response.code()) {
                    case 200: {
                        addOrderToHistoryCallback.onOrderResult(response.body());
                        addOrderToHistoryCallback.onOrderResultCode(response.code());
                    }
                    default: {
                        addOrderToHistoryCallback.onOrderResultCode(response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                addOrderToHistoryCallback.onOrderResultCode(500);
                Log.i("ERROR", t.getMessage());
            }
        });
    }

    public interface AddOrderToHistoryCallback {
        void onOrderResultCode(Integer resultCode);
        void onOrderResult(ResponseModel result);
    }

    public static void onAddOrderToHistoryCallback(AddOrderToHistoryCallback callback) {
        addOrderToHistoryCallback = callback;
    }
}
