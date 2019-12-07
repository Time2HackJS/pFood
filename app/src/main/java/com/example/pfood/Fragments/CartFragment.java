package com.example.pfood.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pfood.Classes.AppSettings;
import com.example.pfood.Classes.FoodCartAdapter;
import com.example.pfood.Classes.FoodCollectable;
import com.example.pfood.R;

import org.w3c.dom.Text;

public class CartFragment extends Fragment {

    private View rootView;
    private Spinner spinner;
    private FoodCartAdapter foodCartAdapter;
    private ListView foodListView;
    private TextView tvPrice;
    String[] data = {"Картой", "Наличными"};

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_cart, container, false);

        AppSettings.getInstance().fullPrice = rootView.findViewById(R.id.cfPrice);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_item, data);

        spinner = rootView.findViewById(R.id.payment_spinner);
        spinner.setAdapter(adapter);

        AppSettings.getInstance().fullNumPrice = 200;
        for (FoodCollectable f : AppSettings.getInstance().foodCart) {
            AppSettings.getInstance().fullNumPrice += f.getFullPrice();
        }
        AppSettings.getInstance().fullPrice.setText(AppSettings.getInstance().fullNumPrice + " \u20BD");

        foodListView = rootView.findViewById(R.id.listview_food);
        foodCartAdapter = new FoodCartAdapter(getActivity(), R.layout.foodcart_view_layout, AppSettings.getInstance().foodCart);
        foodListView.setAdapter(foodCartAdapter);
        AppSettings.getInstance().foodListView = foodListView;

        if (!AppSettings.getInstance().foodCart.isEmpty()) {
            ViewGroup.LayoutParams lp = foodListView.getLayoutParams();
            lp.height = 322 * AppSettings.getInstance().foodCart.size();
            foodListView.setLayoutParams(lp);
        }

        spinner.setPrompt("Выберите способ оплаты");
        spinner.setSelection(1);

        getActivity().setTitle("Корзина");


        return rootView;
    }
}
