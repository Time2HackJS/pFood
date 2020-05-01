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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter<Order> {

    private Context mContext;
    int mResource;

    public OrderAdapter(Context context, int resource, ArrayList<Order> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        String time = getItem(position).getTime();
        String phone = getItem(position).getPhone();
        String address = getItem(position).getAddress();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = convertView.findViewById(R.id.order_name);
        TextView tvTime = convertView.findViewById(R.id.order_time);
        TextView tvPhone = convertView.findViewById(R.id.order_phone);
        TextView tvAddress = convertView.findViewById(R.id.order_address);

        tvName.setText(name);
        tvTime.setText(time);
        tvPhone.setText(phone);
        tvAddress.setText(address);

        return convertView;
    }
}
