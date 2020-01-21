package com.example.pfood.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pfood.R;
import com.example.pfood.model.TeamItem;
import com.example.pfood.model.UserItem;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CreateTeamFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_team, container, false);
        Button saveButton = view.findViewById(R.id.save_team_btn);
        final EditText editText = view.findViewById(R.id.team_name_et);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final HashMap<String, String> userIdMap = new HashMap<String, String>();
                userIdMap.put("userId", FirebaseAuth.getInstance().getUid());

                DatabaseReference userRef = database.getReference("users").child(FirebaseAuth.getInstance().getUid());
                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<UserItem> generic
                                = new GenericTypeIndicator<UserItem>() {
                        };

                        UserItem userValue = dataSnapshot.getValue(generic);

                        if (userValue == null) {
                            Snackbar.make(view, R.string.fill_personal_data, Snackbar.LENGTH_LONG).show();
                        } else {
                            TeamItem teamItem = new TeamItem(
                                    userIdMap,
                                    editText.getText().toString(),
                                    0L,
                                    0L
                            );

                            DatabaseReference myRef = database.getReference("teams").child(userValue.inviteCode);
                            myRef.setValue(teamItem);

                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Snackbar.make(view, "Успешно", Snackbar.LENGTH_SHORT).show();
                                    getActivity().getSupportFragmentManager().popBackStack();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Snackbar.make(view, "Ошибка", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
        return view;
    }
}
