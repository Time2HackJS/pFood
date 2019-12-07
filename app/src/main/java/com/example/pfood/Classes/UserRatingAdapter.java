package com.example.pfood.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pfood.R;

import java.util.ArrayList;
import java.util.List;

public class UserRatingAdapter extends ArrayAdapter<User> {

    private Context mContext;
    private Integer mResource;

    public UserRatingAdapter(Context context, int resource, ArrayList<User> objects) {
        super(context, resource, objects);

        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        int points = getItem(position).getPoints();
        int top_tier = getItem(position).getTopTier();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvRating = convertView.findViewById(R.id.user_rating);
        TextView tvName = convertView.findViewById(R.id.user_name);
        TextView tvPoints = convertView.findViewById(R.id.user_points);

        tvRating.setText(Integer.toString(top_tier));
        tvName.setText(name);
        tvPoints.setText(Integer.toString(points));

        return convertView;
    }
}
