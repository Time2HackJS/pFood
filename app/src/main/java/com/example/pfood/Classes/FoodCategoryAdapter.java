package com.example.pfood.Classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfood.Fragments.FoodFragment;
import com.example.pfood.R;

import java.util.List;

public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<FoodCategory> mData;

    public FoodCategoryAdapter(Context mContext, List<FoodCategory> mData) {
        this.mContext = mContext;
        this.mData = mData;
        Log.i("ADAPTER_TEST", "ADAPTER INIT");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.i("ADAPTER_TEST", "VIEWHOLDER CREATE");
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_category, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        Log.i("ADAPTER_TEST", "ITEM INSERTED");
        holder.tv_name.setText(mData.get(position).getName());
        holder.iv_image.setImageResource(mData.get(position).getImageSource());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppSettings.getInstance().clickedCategory = mData.get(position);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                        R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container, new FoodFragment()).addToBackStack(null).commit();


            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        ImageView iv_image;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.category_name);
            iv_image = itemView.findViewById(R.id.category_image);
            cardView = itemView.findViewById(R.id.cardview_id);
        }
    }
}
