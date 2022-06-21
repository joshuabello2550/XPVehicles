package com.example.xpvehicles.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.AddVehicleActivity;
import com.example.xpvehicles.adapters.VehiclesAdapter;
import com.example.xpvehicles.models.Vehicle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class ExploreFragment extends Fragment {

    private static final String TAG = "Explore_Fragment";
    private VehiclesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_explore, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        FloatingActionButton fabAddVehicle = view.findViewById(R.id.fabAddVehicle);
        bindAdapter(view);
        queryVehicles();
        setAddVehicleOnClickListener(fabAddVehicle);
    }

    private void bindAdapter(View view) {
        RecyclerView rvVehicles = view.findViewById(R.id.rvVehicles);
        List<Vehicle> allVehicles = new ArrayList<>();
        adapter = new VehiclesAdapter(allVehicles);
        rvVehicles.setAdapter(adapter);
        rvVehicles.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
    }

    private void setAddVehicleOnClickListener(FloatingActionButton fabAddVehicle) {
        fabAddVehicle.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), AddVehicleActivity.class);
            startActivity(i);
        });
    }

    private void queryVehicles() {
        ParseQuery<Vehicle> query = ParseQuery.getQuery(Vehicle.class);
        query.findInBackground(new FindCallback<Vehicle>() {
            @Override
            public void done(List<Vehicle> vehicles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting the vehicles",e);
                    return;
                }
                adapter.addAll(vehicles);
            }
        });

    }

}