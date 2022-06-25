package com.example.xpvehicles.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.adapters.RentingVehiclesAdapter;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models.Vehicle;
import com.example.xpvehicles.models._User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RentingVehiclesFragment extends Fragment {

    private static final String TAG = "Vehicles_Fragment";
    private RentingVehiclesAdapter adapter;

    public RentingVehiclesFragment(MainActivity mainActivity) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_renting_vehicles, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        bindAdapter(view);
        queryVehicles();
    }

    private void bindAdapter(View view) {
        List<RentVehicle> allVehicles = new ArrayList<>();
        adapter = new RentingVehiclesAdapter(this, allVehicles, (MainActivity)getActivity());

        RecyclerView rvVehicles = view.findViewById(R.id.rvRentingVehicles);
        rvVehicles.setAdapter(adapter);
        rvVehicles.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void queryVehicles() {
        _User currentUser = (_User) ParseUser.getCurrentUser();
        ParseQuery<RentVehicle> query = ParseQuery.getQuery(RentVehicle.class);
        query.whereEqualTo("rentee", currentUser);

        query.findInBackground(new FindCallback<RentVehicle>() {
            @Override
            public void done(List<RentVehicle> vehicles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting the vehicles",e);
                    return;
                }
                adapter.addAll(vehicles);
            }
        });

    }





}