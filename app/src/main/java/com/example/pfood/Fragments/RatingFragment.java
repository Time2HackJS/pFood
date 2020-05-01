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

import com.example.pfood.Classes.AppSettings;
import com.example.pfood.Classes.Team;
import com.example.pfood.Classes.User;
import com.example.pfood.Classes.TeamRatingAdapter;
import com.example.pfood.NetworkClasses.NetworkTeams;
import com.example.pfood.R;
import com.example.pfood.ResponseModels.TeamModel;
import com.example.pfood.navigation.Router;

import java.util.ArrayList;
import java.util.List;

public class RatingFragment extends Fragment implements NetworkTeams.GetTeamsCallback {

    private View rootView;
    private ArrayList<User> userList;
    private TeamRatingAdapter mAdapter;
    private ListView ratingListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("FRAGMENT_TEST", "ENTERED FOOD FRAGMENT");
        rootView = inflater.inflate(R.layout.fragment_rating, container, false);

        userList = new ArrayList<>();

        ratingListView = rootView.findViewById(R.id.rating_listview);

        getActivity().setTitle("Рейтинг");

        Button myTeamButton = rootView.findViewById(R.id.rating_my_team_btn);
        myTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Router.openFragmentSimply(getActivity(), R.id.fragment_container, new MyTeamFragment());
            }
        });

        NetworkTeams.onGetTeamsCallback(this);
        NetworkTeams.getTeams();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResultCode(Integer resultCode) {
        Log.i("NANIDESUKA", resultCode.toString());
    }

    @Override
    public void onResult(List<TeamModel> result) {
        ArrayList<Team> teamList = new ArrayList<>();
        int position = 1;

        for (TeamModel tm : result) {
            teamList.add(new Team(tm.name, position, Integer.parseInt(tm.point)));
            position++;
        }

        mAdapter = new TeamRatingAdapter(getActivity(), R.layout.rating_user_view_layout, teamList);
        ratingListView.setAdapter(mAdapter);

        AppSettings.getInstance().teamList = teamList;
    }
}
