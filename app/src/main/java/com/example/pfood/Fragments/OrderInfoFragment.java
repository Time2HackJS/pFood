package com.example.pfood.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.pfood.Classes.AppSettings;
import com.example.pfood.Classes.AppUsers;
import com.example.pfood.Classes.Food;
import com.example.pfood.Classes.FoodCollectable;
import com.example.pfood.Classes.Order;
import com.example.pfood.NetworkClasses.NetworkOrders;
import com.example.pfood.NetworkClasses.NetworkTeams;
import com.example.pfood.NetworkClasses.NetworkUsers;
import com.example.pfood.R;
import com.example.pfood.ResponseModels.ResponseModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class OrderInfoFragment extends Fragment implements NetworkUsers.AddUserPointsCallback, NetworkOrders.AddOrderToHistoryCallback {

    private View rootView;
    private ArrayAdapter<String> adapter;

    private TextView name;
    private TextView phone;
    private TextView price;
    private ListView listView;
    private TextView address;
    private EditText commentary;
    private TextView time;
    private TextView paymentType;
    private Button button, buttonEdit, buttonApply, buttonDelete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.order_description, container, false);

        name = rootView.findViewById(R.id.order_name);
        phone = rootView.findViewById(R.id.order_phone);
        price = rootView.findViewById(R.id.order_price);
        listView = rootView.findViewById(R.id.food_listview);
        address = rootView.findViewById(R.id.order_address);
        commentary = rootView.findViewById(R.id.order_commentary);
        button = rootView.findViewById(R.id.order_button);
        time = rootView.findViewById(R.id.order_time);
        paymentType = rootView.findViewById(R.id.order_payment);
        buttonEdit = rootView.findViewById(R.id.order_edit);
        buttonApply = rootView.findViewById(R.id.order_apply);
        buttonDelete = rootView.findViewById(R.id.order_delete);

        name.setText(AppSettings.getInstance().clickedOrder.getName());
        phone.setText(AppSettings.getInstance().clickedOrder.getPhone());
        time.setText(AppSettings.getInstance().clickedOrder.getTime());
        address.setText(AppSettings.getInstance().clickedOrder.getAddress());
        commentary.setText(AppSettings.getInstance().clickedOrder.getCommentary());
        paymentType.setText(AppSettings.getInstance().clickedOrder.getPaymentType());

        if (AppSettings.getInstance().clickedOrder.getPrice() > AppSettings.getInstance().freeDeliveryCost)
            price.setText(AppSettings.getInstance().clickedOrder.getPrice().toString() + "\u20BD");
        else
            price.setText((AppSettings.getInstance().clickedOrder.getPrice() + AppSettings.getInstance().deliveryCost) + "\u20BD");

        Log.i("CHECKCOMMENT", AppSettings.getInstance().clickedOrder.getCommentary());

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, AppSettings.getInstance().clickedOrder.getFoodCart());
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        NetworkUsers.onAddUserPointsCallback(this);
        NetworkOrders.onAddOrderToHistoryCallback(this);

        if (!AppSettings.getInstance().chefIds.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            buttonEdit.setVisibility(View.INVISIBLE);
            buttonApply.setVisibility(View.INVISIBLE);

            commentary.setClickable(false);
            commentary.setFocusable(false);
        }

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > 22) {

                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 101);

                        return;
                    }
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone.getText().toString().trim()));
                    startActivity(callIntent);
                } else {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone.getText().toString().trim()));
                    startActivity(callIntent);
                }
            }
        });

        phone.setPaintFlags(phone.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSettings.getInstance().clickedOrder.setCommentary(commentary.getText().toString());
                FirebaseDatabase.getInstance().getReference("orders").child(AppSettings.getInstance().clickedOrder.getKey())
                        .setValue(AppSettings.getInstance().clickedOrder);

                Toast.makeText(getContext(), "Заказ был успешно изменен!", Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.alert_yes_no, null);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mView.findViewById(R.id.alert_yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference("orders").child(AppSettings.getInstance().clickedOrder.getKey())
                                .removeValue();

                        NetworkUsers.addUserPoints(AppSettings.getInstance().clickedOrder.getUserId(),
                                                   Integer.toString(AppSettings.getInstance().clickedOrder.getPrice() / 5));

                        String order_p = "";
                        for (String s : AppSettings.getInstance().clickedOrder.getFoodCart())
                            order_p += s + " | ";

                        order_p = order_p.substring(0, order_p.length() - 3);

                        NetworkOrders.addOrderToHistory(
                                AppSettings.getInstance().clickedOrder,
                                order_p,
                                Calendar.getInstance().getTime().toString().substring(11, 19),
                                "Выполнено"
                        );

                        if (AppSettings.getInstance().orderList.size() == 1)
                            AppSettings.getInstance().menuItem.setTitle("Заказы");

                        AppSettings.getInstance().orderList.remove(AppSettings.getInstance().clickedOrder);
                        dialog.dismiss();

                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                        R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                                .replace(R.id.fragment_container, new OrderFragment(), "OrderToFragment").addToBackStack(null).commit();

                    }
                });

                mView.findViewById(R.id.alert_no).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.alert_yes_no, null);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mView.findViewById(R.id.alert_yes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference("orders").child(AppSettings.getInstance().clickedOrder.getKey())
                                .removeValue();

                        String order_p = "";
                        for (String s : AppSettings.getInstance().clickedOrder.getFoodCart())
                            order_p += s + " | ";

                        order_p = order_p.substring(0, order_p.length() - 3);

                        NetworkOrders.addOrderToHistory(
                                AppSettings.getInstance().clickedOrder,
                                order_p,
                                Calendar.getInstance().getTime().toString().substring(11, 19),
                                "Отказано"
                        );

                        dialog.dismiss();

                        if (AppSettings.getInstance().orderList.size() == 1)
                            AppSettings.getInstance().menuItem.setTitle("Заказы");

                        AppSettings.getInstance().orderList.remove(AppSettings.getInstance().clickedOrder);

                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                        R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                                .replace(R.id.fragment_container, new OrderFragment(), "OrderToFragment").addToBackStack(null).commit();

                    }
                });

                mView.findViewById(R.id.alert_no).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<FoodCollectable> orderCart = new ArrayList<>();

                for (String s : AppSettings.getInstance().clickedOrder.getFoodCart()) {
                    orderCart.add(new FoodCollectable(AppSettings.getInstance().findSingleFood(s), Integer.parseInt(s.substring(s.lastIndexOf(' ') + 1).replace("шт.", ""))));
                    Log.i("CHECKADDING", orderCart.get(0).getName() + " " + orderCart.get(0).getFoodCount());
                    Log.i("CHECKFOODCART", AppSettings.getInstance().foodCache.get(0).getName());
                }

                AppSettings.getInstance().orderCart = orderCart;

                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container, new OrderEditFragment()).addToBackStack(null).commit();
            }
        });

        return rootView;
    }

    @Override
    public void onResultCode(Integer resultCode) {

    }

    @Override
    public void onResult(ResponseModel result) {
        Log.i("REQUEST", "SUCCESS");
        AppUsers.getInstance().refreshPoints(AppSettings.getInstance().clickedOrder.getUserId());
    }

    @Override
    public void onOrderResult(ResponseModel result) {
        Log.i("HISTORY ADD REQUEST",  result.count + ": " + result.message);
    }

    @Override
    public void onOrderResultCode(Integer resultCode) {
        Log.i("REQUEST", "HISTORY CODE " + resultCode);
    }
}
