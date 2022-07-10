package com.example.xpvehicles.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.adapters.RentingRequestsAdapter;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models._User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class RentingRequestsFragment extends Fragment {

    private static final String TAG = "RentingVehiclesFragment";
    private static final String QUERY_PARAMETER_RENTEE =  "rentee";
    private RentingRequestsAdapter adapter;
    private TextView tvUserNoRentingVehicles;

    public RentingRequestsFragment(MainActivity mainActivity) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_renting_requests, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        bind();
        queryVehicles();
        bindAdapter(view);
    }

    private void bind() {
        tvUserNoRentingVehicles = getActivity().findViewById(R.id.tvUserNoRentingVehicles);
    }

    private void bindAdapter(View view) {
        List<RentVehicle> allVehicles = new ArrayList<>();
        adapter = new RentingRequestsAdapter(this, allVehicles, (MainActivity)getActivity());

        RecyclerView rvVehicles = view.findViewById(R.id.rvRentingVehicles);
        rvVehicles.setAdapter(adapter);
        rvVehicles.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void queryVehicles() {
        _User currentUser = (_User) ParseUser.getCurrentUser();
        ParseQuery<RentVehicle> query = ParseQuery.getQuery(RentVehicle.class);
        query.whereEqualTo(QUERY_PARAMETER_RENTEE, currentUser);

        query.findInBackground(new FindCallback<RentVehicle>() {
            @Override
            public void done(List<RentVehicle> vehicles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting the vehicles",e);
                    return;
                }
                adapter.setVehicles(vehicles, tvUserNoRentingVehicles);
            }
        });

    }
}