package com.example.pfood.Classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pfood.Fragments.DescriptionFragment;
import com.example.pfood.R;
import com.example.pfood.model.FoodItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Food> mData;

    private static final String TAG = "FoodAdapter";

    public FoodAdapter(Context mContext) {
        this.mContext = mContext;
        mData = new ArrayList<>();
        String categoryName = AppSettings.getInstance().clickedCategory.getName();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("categories/" + categoryName);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                GenericTypeIndicator<ArrayList<FoodItem>> generic
                        = new GenericTypeIndicator<ArrayList<FoodItem>>() {
                };

                ArrayList<FoodItem> value = dataSnapshot.getValue(generic);

                if (value != null) {
                    for (FoodItem item : value) {
                        if (item != null) {
                            mData.add(new Food(item.getName(), (int) item.getPrice(), item.getDescription(), mData.size(), R.drawable.testimage_2));
                        }
                    }
                }

                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: Error" + databaseError.toString());
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.i("ADAPTER_TEST", "FOOD VIEWHOLDER CREATE");
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_food_category, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Log.i("ADAPTER_TEST", "FOOD ITEM INSERTED");
        holder.tv_name.setText(mData.get(position).getName());
        holder.iv_image.setImageResource(mData.get(position).getImageSource());
        holder.bPrice.setText(mData.get(position).getPrice().toString());

        if (AppSettings.getInstance().findCollectable(mData.get(position)) != null &&
            AppSettings.getInstance().findCollectable(mData.get(position)).getFoodCount() != 0) {

            holder.bAdd.setVisibility(View.VISIBLE);
            holder.bRemove.setVisibility(View.VISIBLE);
            holder.countText.setVisibility(View.VISIBLE);
            holder.countLinear.setVisibility(View.VISIBLE);

            holder.bPrice.setVisibility(View.INVISIBLE);

            holder.countText.setText(AppSettings.getInstance().countOf(mData.get(position)).toString());
        } else
        {
            holder.bAdd.setVisibility(View.INVISIBLE);
            holder.bRemove.setVisibility(View.INVISIBLE);
            holder.countText.setVisibility(View.INVISIBLE);
            holder.countLinear.setVisibility(View.INVISIBLE);

            holder.bPrice.setVisibility(View.VISIBLE);
        }

        holder.bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSettings.getInstance().findCollectable(mData.get(position)).incCount();
                notifyDataSetChanged();

                AppSettings.getInstance().tvNum.setVisibility(View.VISIBLE);
                AppSettings.getInstance().ivCircle.setVisibility(View.VISIBLE);

                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale);
                AppSettings.getInstance().tvNum.startAnimation(anim);

                AppSettings.getInstance().tvNum.setText(AppSettings.getInstance().fullNumPrice + " \u20BD");
            }
        });

        holder.bRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSettings.getInstance().findCollectable(mData.get(position)).decCount();
                notifyDataSetChanged();

                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_remove);
                AppSettings.getInstance().tvNum.startAnimation(anim);

                AppSettings.getInstance().tvNum.setText(AppSettings.getInstance().fullNumPrice + " \u20BD");

                if (AppSettings.getInstance().findCollectable(mData.get(position)) == null && AppSettings.getInstance().foodCart.isEmpty()) {
                    AppSettings.getInstance().deleteCollectable(mData.get(position));

                    AppSettings.getInstance().tvNum.setVisibility(View.INVISIBLE);
                    AppSettings.getInstance().ivCircle.setVisibility(View.INVISIBLE);

                    AppSettings.getInstance().tvNum.setText(" ");
                }
            }
        });

        holder.bPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodCollectable clickedFood = new FoodCollectable(mData.get(position), 1);
                AppSettings.getInstance().fullNumPrice += mData.get(position).getPrice();
                AppSettings.getInstance().foodCache.add(mData.get(position));
                AppSettings.getInstance().foodCart.add(clickedFood);

                AppSettings.getInstance().tvNum.setVisibility(View.VISIBLE);
                AppSettings.getInstance().ivCircle.setVisibility(View.VISIBLE);

                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale);
                AppSettings.getInstance().tvNum.startAnimation(anim);
                AppSettings.getInstance().tvNum.setText(AppSettings.getInstance().fullNumPrice + " \u20BD");

                holder.countText.setText("1");

                notifyDataSetChanged();
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppSettings.getInstance().clickedFood = mData.get(position);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                activity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                        .replace(R.id.fragment_container, new DescriptionFragment()).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, countText;
        ImageView iv_image;
        CardView cardView;
        Button bPrice;
        ImageButton bAdd, bRemove;
        LinearLayout countLinear;

        public MyViewHolder(View itemView) {
            super(itemView);

            countText = itemView.findViewById(R.id.count_text);
            tv_name = itemView.findViewById(R.id.category_name);
            iv_image = itemView.findViewById(R.id.category_image);
            cardView = itemView.findViewById(R.id.cardview_id);
            bPrice = itemView.findViewById(R.id.button_price);
            bAdd = itemView.findViewById(R.id.b_add);
            bRemove = itemView.findViewById(R.id.b_remove);
            countLinear = itemView.findViewById(R.id.count_linear);
        }
    }
}
