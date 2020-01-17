package com.example.pfood.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.pfood.navigation.Router;
import com.example.pfood.utils.FirebaseUtils;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

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

        ArrayList<FoodCollectable> foodCollectable = new ArrayList<>();
        for (FoodCollectable f : AppSettings.getInstance().foodCart) {
            if (f.getFoodCount() != 0)
            {
                foodCollectable.add(f);
            }
        }
        foodListView = rootView.findViewById(R.id.listview_food);
        foodCartAdapter = new FoodCartAdapter(getActivity(), R.layout.foodcart_view_layout, foodCollectable);
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

        Button orderButton = rootView.findViewById(R.id.order);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (FirebaseUtils.isUserLoggedIn()) {
                    Snackbar.make(view, "TODO" + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(), Snackbar.LENGTH_SHORT)
                            .setAction("Logout", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AuthUI.getInstance().signOut(getContext());
                                }
                            })
                            .show();
                } else {
                    Snackbar.make(view, R.string.order_snack_please_login_text, Snackbar.LENGTH_LONG)
                            .setAction(R.string.order_snacl_login_action, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Fragment fragment = new ProfileFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean(ProfileFragment.KEY_IS_OPEN_FIREBASE, true);
                                    fragment.setArguments(bundle);
                                    Router.openFragmentSimply(getActivity(), R.id.fragment_container, fragment);
                                }
                            })
                            .show();
                }

            }
        });


        return rootView;
    }
}
