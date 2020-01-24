package com.example.pfood.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
        String imageUrl = getItem(position).getImageUrl();

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

                AppSettings.getInstance().fullPrice.setText(AppSettings.getInstance().fullNumPrice + " \u20BD");
                AppSettings.getInstance().foodCount++;

                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale);
                AppSettings.getInstance().tvNum.startAnimation(anim);
                AppSettings.getInstance().tvNum.setText(AppSettings.getInstance().fullNumPrice + " \u20BD");

                notifyDataSetChanged();
                AppSettings.getInstance().foodAdapter.notifyDataSetChanged();
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
                if (getItem(position).getFoodCount().equals(0)) {
                    AppSettings.getInstance().foodCart.remove(getItem(position));
                    AppSettings.getInstance().deleteCollectable(getItem(position));
                }

                AppSettings.getInstance().foodCount--;

                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_remove);
                AppSettings.getInstance().tvNum.startAnimation(anim);
                AppSettings.getInstance().tvNum.setText(AppSettings.getInstance().fullNumPrice + " \u20BD");

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
                    AppSettings.getInstance().foodListView.setVisibility(View.GONE);
                }

                notifyDataSetChanged();
                AppSettings.getInstance().foodAdapter.notifyDataSetChanged();
            }
        });

        tvName.setText(name);
        tvCount.setText(Integer.toString(count));
        tvPrice.setText(fullPrice + "\u20BD");
        Glide.with(imageView).load(imageUrl).into(imageView);
        AppSettings.getInstance().fullPrice.setText(AppSettings.getInstance().fullNumPrice + " \u20BD");



        return convertView;
    }
}
