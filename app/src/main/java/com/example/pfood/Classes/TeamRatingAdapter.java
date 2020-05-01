package com.example.pfood.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pfood.R;

import java.util.ArrayList;
import java.util.List;

public class TeamRatingAdapter extends ArrayAdapter<Team> {

    private Context mContext;
    private Integer mResource;

    public TeamRatingAdapter(Context context, int resource, ArrayList<Team> objects) {
        super(context, resource, objects);

        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        int points = getItem(position).getTeamRating();
        int top_tier = getItem(position).getTeamPlace();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvRating = convertView.findViewById(R.id.user_rating);
        TextView tvName = convertView.findViewById(R.id.user_name);
        TextView tvPoints = convertView.findViewById(R.id.user_points);
        LinearLayout rLinear = convertView.findViewById(R.id.top_linear);

        if (top_tier == 4) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 60, 0, 0);
            rLinear.setLayoutParams(lp);
        }

        tvRating.setText(Integer.toString(top_tier));
        tvName.setText(name);
        tvPoints.setText(Integer.toString(points));

        return convertView;
    }
}
