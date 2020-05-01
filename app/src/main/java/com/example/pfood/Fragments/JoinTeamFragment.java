package com.example.pfood.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pfood.NetworkClasses.NetworkTeams;
import com.example.pfood.NetworkClasses.NetworkUsers;
import com.example.pfood.R;
import com.example.pfood.ResponseModels.ResponseModel;
import com.example.pfood.ResponseModels.TeamModel;
import com.example.pfood.ResponseModels.UserTeamModel;
import com.example.pfood.model.TeamItem;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinTeamFragment extends Fragment implements NetworkUsers.AddUserToTeamCallback, NetworkUsers.GetUserTeamCallback {

    public String team_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_team, container, false);
        Button saveButton = view.findViewById(R.id.join_team_btn);
        final EditText editText = view.findViewById(R.id.invite_code_et);

        NetworkUsers.onAddUserToTeamCallback(this);
        NetworkUsers.onGetUserTeamCallback(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                team_id = editText.getText().toString();

                NetworkUsers.getUserTeam(FirebaseAuth.getInstance().getCurrentUser().getUid());
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("teams").child(editText.getText().toString());

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        GenericTypeIndicator<TeamItem> generic
                                = new GenericTypeIndicator<TeamItem>() {
                        };

                        final TeamItem teamValue = dataSnapshot.getValue(generic);

                        if (teamValue != null) {
                            Map<String, Object> update = new HashMap<>();
                            update.put("inviteCode", editText.getText().toString());
                            database.getReference("users").child(FirebaseAuth.getInstance().getUid()).updateChildren(
                                    update
                            );

                            Toast.makeText(getContext(), "Успешно", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();

                        } else {
                            Toast.makeText(getContext(), "Такой команды нет", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Snackbar.make(view, "Ошибка", Snackbar.LENGTH_SHORT).show();
                    }
                });

                NetworkUsers.getUserTeam(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
        });
        return view;
    }

    @Override
    public void onResultCode(Integer resultCode) {

    }

    @Override
    public void onResult(UserTeamModel result) {
        if (Integer.parseInt(result.user_id_team) <= 0) {
            NetworkUsers.addUserToTeam(FirebaseAuth.getInstance().getCurrentUser().getUid(), team_id);
        }
    }

    @Override
    public void onAddResult(ResponseModel result) {
        Log.i("SUCCESS", "ADDED TO TEAM");
    }
}
