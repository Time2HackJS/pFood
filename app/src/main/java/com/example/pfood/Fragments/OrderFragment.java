package com.example.pfood.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.pfood.Classes.AppSettings;
import com.example.pfood.Classes.IOnBackPressed;
import com.example.pfood.Classes.Order;
import com.example.pfood.Classes.OrderAdapter;
import com.example.pfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderFragment extends Fragment implements IOnBackPressed {

    private View rootView;
    private ListView listView;
    private OrderAdapter adapter;
    private static final String TAG = "OrderFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.order_layout, container, false);

        getActivity().setTitle("Заказы");

        listView = rootView.findViewById(R.id.order_listview);
        adapter = new OrderAdapter(getActivity(), R.layout.order_view_layout, AppSettings.getInstance().orderList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        AppSettings.getInstance().orderAdapter = adapter;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppSettings.getInstance().clickedOrder = (Order) adapterView.getItemAtPosition(i);

                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container, new OrderInfoFragment()).addToBackStack(null).commit();

            }
        });

        return rootView;
    }

    @Override
    public void onBackPressed() {}
}
