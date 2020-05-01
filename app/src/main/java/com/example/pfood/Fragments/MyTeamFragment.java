package com.example.pfood.Fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pfood.Classes.AppSettings;
import com.example.pfood.Classes.AppUsers;
import com.example.pfood.Classes.Team;
import com.example.pfood.Classes.User;
import com.example.pfood.NetworkClasses.NetworkUsers;
import com.example.pfood.R;
import com.example.pfood.ResponseModels.UserTeamModel;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MyTeamFragment extends Fragment {

    private static final String TAG = "MyTeamFragment";

    private TextView teamName;
    private TextView teamPlace;
    private TextView teamRating;
    private TextView teamInviteCode;
    private Button createTeam;
    private Button joinTeam;
    private TextView membersList;
    private ArrayList<String> users = new ArrayList<>();
    private String usersString = "";

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
        membersList = view.findViewById(R.id.team_members_list);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("teams");

        teamName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Router.openFragmentSimply(getActivity(), R.id.fragment_container, new CreateTeamFragment());
            }
        });

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

                            final UserItem userValue = dataSnapshot.getValue(generic);

                            if (userValue == null) {
                                Snackbar.make(getView(), R.string.fill_personal_data, Snackbar.LENGTH_LONG).show();
                            }

                            if (teamsValue.get(userValue.inviteCode) != null) {
                                joinTeam.setVisibility(View.GONE);
                                createTeam.setVisibility(View.GONE);
                                TeamItem item = teamsValue.get(userValue.inviteCode);

                                for (Team t : AppSettings.getInstance().teamList) {
                                    if (t.getName().equals(item.teamName)) {
                                        teamPlace.setText(t.getTeamPlace().toString());
                                        teamRating.setText(t.getTeamRating().toString());
                                    }
                                }

                                teamName.setText(item.teamName);
                                teamInviteCode.setText(userValue.inviteCode);
                                teamInviteCode.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText("", userValue.inviteCode);
                                        clipboard.setPrimaryClip(clip);
                                        Toast.makeText(getContext(), "Пригласительный код скопирован в буфер обмена!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                //fill members list
                                for (String uId : item.teamMembers.values()) {
                                    database.getReference("users").child(uId).addValueEventListener(
                                            new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (User u : AppUsers.getInstance().userList) {
                                                        if (u.getInviteCode().equals(userValue.inviteCode))
                                                            users.add(u.getRating() + " " + u.getName());
                                                    }
                                                    Collections.sort(users);

                                                    for (String u : users) {
                                                        usersString += u.substring(u.indexOf(" ")) + " " + u.substring(0, u.indexOf(" ")) + " баллов\n";
                                                    }

                                                    membersList.setText(usersString);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            }
                                    );
                                }
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

                            }

                            Log.i(TAG, "onDataChange: user is " + userValue);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Snackbar.make(getView(), "Авторизуйтесь", Snackbar.LENGTH_SHORT).show();
                    teamName.setVisibility(View.GONE);
                    teamPlace.setVisibility(View.GONE);
                    teamRating.setVisibility(View.GONE);
                    teamInviteCode.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: Error" + databaseError.toString());
            }
        });
    }
}
