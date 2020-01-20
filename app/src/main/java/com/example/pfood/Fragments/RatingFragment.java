package com.example.pfood.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pfood.Classes.User;
import com.example.pfood.Classes.UserRatingAdapter;
import com.example.pfood.R;
import com.example.pfood.navigation.Router;

import java.util.ArrayList;

public class RatingFragment extends Fragment {

    private View rootView;
    private ArrayList<User> userList;
    private UserRatingAdapter mAdapter;
    private ListView ratingListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("FRAGMENT_TEST", "ENTERED FOOD FRAGMENT");
        rootView = inflater.inflate(R.layout.fragment_rating, container, false);

        userList = new ArrayList<>();
        userList.add(new User(1, "Виталя", 1337));
        userList.add(new User(2, "Евпатий", 322));
        userList.add(new User(3, "Гриша", 228));

        ratingListView = rootView.findViewById(R.id.rating_listview);
        mAdapter = new UserRatingAdapter(getActivity(), R.layout.rating_user_view_layout, userList);

        ratingListView.setAdapter(mAdapter);

        getActivity().setTitle("Рейтинг пользователей");

        Button myTeamButton = rootView.findViewById(R.id.rating_my_team_btn);
        myTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Router.openFragmentSimply(getActivity(), R.id.fragment_container, new MyTeamFragment());
            }
        });

        return rootView;
    }
}
