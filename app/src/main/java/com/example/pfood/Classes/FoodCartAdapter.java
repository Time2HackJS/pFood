package com.example.pfood.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pfood.R;

import java.util.ArrayList;

public class FoodCartAdapter extends ArrayAdapter<FoodCollectable> {

    private Context mContext;
    int mResource;

    public FoodCartAdapter(Context context, int resource, ArrayList<FoodCollectable> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        int fullPrice = getItem(position).getFullPrice();
        int count = getItem(position).getFoodCount();
        int imageSource = getItem(position).getImageSource();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = convertView.findViewById(R.id.cartFoodName);
        ImageView imageView = convertView.findViewById(R.id.cartFoodImage);
        TextView tvCount = convertView.findViewById(R.id.cartFoodCount);
        TextView tvPrice = convertView.findViewById(R.id.cartFoodPrice);
        ImageButton bAdd = convertView.findViewById(R.id.button_add);
        ImageButton bRemove = convertView.findViewById(R.id.button_remove);

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItem(position).setFoodCount(getItem(position).getFoodCount() + 1);

                AppSettings.getInstance().fullNumPrice = 200;
                for (FoodCollectable f : AppSettings.getInstance().foodCart) {
                    AppSettings.getInstance().fullNumPrice += f.getFullPrice();
                }

                AppSettings.getInstance().fullPrice.setText(Integer.toString(AppSettings.getInstance().fullNumPrice));
                AppSettings.getInstance().foodCount++;
                AppSettings.getInstance().tvNum.setText(Integer.toString(AppSettings.getInstance().foodCount));

                notifyDataSetChanged();
            }
        });

        bRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getItem(position).setFoodCount(getItem(position).getFoodCount() - 1);

                AppSettings.getInstance().fullNumPrice = 200;
                for (FoodCollectable f : AppSettings.getInstance().foodCart) {
                    AppSettings.getInstance().fullNumPrice += f.getFullPrice();
                }

                AppSettings.getInstance().fullPrice.setText(AppSettings.getInstance().fullNumPrice + " \u20BD");
                if (getItem(position).getFoodCount().equals(0))
                    AppSettings.getInstance().foodCart.remove(getItem(position));

                AppSettings.getInstance().foodCount--;
                AppSettings.getInstance().tvNum.setText(Integer.toString(AppSettings.getInstance().foodCount));

                if (AppSettings.getInstance().foodCount.equals(0)) {
                    AppSettings.getInstance().tvNum.setVisibility(View.INVISIBLE);
                    AppSettings.getInstance().ivCircle.setVisibility(View.INVISIBLE);
                }

                if (!AppSettings.getInstance().foodCart.isEmpty()) {
                    ViewGroup.LayoutParams lp = AppSettings.getInstance().foodListView.getLayoutParams();
                    lp.height = 322 * AppSettings.getInstance().foodCart.size();
                    AppSettings.getInstance().foodListView.setLayoutParams(lp);
                }
                else {
                    ViewGroup.LayoutParams lp = AppSettings.getInstance().foodListView.getLayoutParams();
                    lp.height = 0;
                    AppSettings.getInstance().foodListView.setLayoutParams(lp);
                }

                notifyDataSetChanged();
            }
        });

        tvName.setText(name);
        tvCount.setText(Integer.toString(count));
        tvPrice.setText(fullPrice + "\u20BD");
        imageView.setImageResource(imageSource);
        AppSettings.getInstance().fullPrice.setText(AppSettings.getInstance().fullNumPrice + " \u20BD");



        return convertView;
    }
}