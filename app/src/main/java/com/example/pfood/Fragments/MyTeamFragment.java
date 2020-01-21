package com.example.pfood.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pfood.R;
import com.example.pfood.model.TeamItem;
import com.example.pfood.model.UserItem;
import com.example.pfood.navigation.Router;
import com.example.pfood.utils.FirebaseUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MyTeamFragment extends Fragment {

    private static final String TAG = "MyTeamFragment";

    private TextView teamName;
    private TextView teamPlace;
    private TextView teamRating;
    private TextView teamInviteCode;
    private Button createTeam;
    private Button joinTeam;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_my_team, container, false);

        teamName = view.findViewById(R.id.team_name_tv);
        teamPlace = view.findViewById(R.id.team_place_tv);
        teamRating = view.findViewById(R.id.team_rating_tv);
        teamInviteCode = view.findViewById(R.id.team_invite_code_tv);
        createTeam = view.findViewById(R.id.team_create_btn);
        joinTeam = view.findViewById(R.id.team_join_btn);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("teams");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                GenericTypeIndicator<HashMap<String, TeamItem>> generic
                        = new GenericTypeIndicator<HashMap<String, TeamItem>>() {
                };

                final HashMap<String, TeamItem> teamsValue = dataSnapshot.getValue(generic);

                if (FirebaseUtils.isUserLoggedIn()) {
                    DatabaseReference userRef = database.getReference("users").child(FirebaseAuth.getInstance().getUid());
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            GenericTypeIndicator<UserItem> generic
                                    = new GenericTypeIndicator<UserItem>() {
                            };

                            UserItem userValue = dataSnapshot.getValue(generic);

                            if (userValue == null) {
                                Snackbar.make(getView(), R.string.fill_personal_data, Snackbar.LENGTH_LONG).show();
                            }

                            if (teamsValue.get(userValue.inviteCode) != null) {
                                joinTeam.setVisibility(View.GONE);
                                createTeam.setVisibility(View.GONE);
                                TeamItem item = teamsValue.get(userValue.inviteCode);
                                teamName.setText(item.teamName);
                                teamPlace.setText(item.teamPlace.toString());
                                teamRating.setText(item.teamRating.toString());
                                teamInviteCode.setText(userValue.inviteCode);
                            } else {
                                createTeam.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Router.openFragmentSimply(getActivity(), R.id.fragment_container, new CreateTeamFragment());
                                    }
                                });
                                joinTeam.setOnClickListener(
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Router.openFragmentSimply(getActivity(), R.id.fragment_container, new JoinTeamFragment());
                                            }
                                        }
                                );
                                teamName.setVisibility(View.GONE);
                                teamPlace.setVisibility(View.GONE);
                                teamRating.setVisibility(View.GONE);
                                teamInviteCode.setVisibility(View.GONE);
                            }

                            Log.i(TAG, "onDataChange: user is " + userValue);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: Error" + databaseError.toString());
            }
        });
    }
}
