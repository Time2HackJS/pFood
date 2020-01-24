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
import com.example.pfood.model.TeamItem;
import com.example.pfood.model.UserItem;
import com.example.pfood.navigation.Router;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

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



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("teams");

        usersRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<HashMap<String, TeamItem>> generic
                                = new GenericTypeIndicator<HashMap<String, TeamItem>>() {
                        };

                        HashMap<String, TeamItem> teamValue = dataSnapshot.getValue(generic);


                        if (teamValue != null) {
                            mAdapter.clear();
                            for (String key : teamValue.keySet()) {
                                TeamItem teamItem = teamValue.get(key);
                                if (teamItem != null) {
                                    try {
                                        User user = new User(teamItem.teamPlace.intValue(), teamItem.teamName, teamItem.teamRating.intValue());
                                        mAdapter.add(user);
                                    } catch (Exception e) {
                                    }
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
