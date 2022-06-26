package com.example.xpvehicles.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.AddVehicleActivity;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.adapters.ExploreAdapter;
import com.example.xpvehicles.adapters.RecommendedVehiclesAdapter;
import com.example.xpvehicles.models.Vehicle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment {

    private static final String TAG = "Explore_Fragment";
    private ExploreAdapter exploreAdapter;
    private RecommendedVehiclesAdapter recommendedVehiclesAdapter;
    private MainActivity activity;

    public ExploreFragment(MainActivity mainActivity){
        activity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_explore, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        FloatingActionButton fabAddVehicle = view.findViewById(R.id.fabAddVehicle);
        bindExploreAdapter(view);
        bindRecommendedAdapter(view);
        queryVehicles();
        setAddVehicleOnClickListener(fabAddVehicle);
    }

    private void bindRecommendedAdapter(View view) {
        List<Vehicle> recommendedVehicles = new ArrayList<>();
        recommendedVehiclesAdapter = new RecommendedVehiclesAdapter(this, recommendedVehicles, (MainActivity)getActivity());

        RecyclerView rvRecommendedVehicles = view.findViewById(R.id.rvRecommendedVehicles);
        rvRecommendedVehicles.setAdapter(recommendedVehiclesAdapter);
        rvRecommendedVehicles.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    private void bindExploreAdapter(View view) {
        List<Vehicle> allVehicles = new ArrayList<>();
        exploreAdapter = new ExploreAdapter(this, allVehicles, (MainActivity)getActivity());

        RecyclerView rvVehicles = view.findViewById(R.id.rvExplore);
        rvVehicles.setAdapter(exploreAdapter);
        rvVehicles.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
    }

    private void setAddVehicleOnClickListener(FloatingActionButton fabAddVehicle) {
        fabAddVehicle.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), AddVehicleActivity.class);
            startActivity(i);
        });
    }

    private void queryVehicles() {
        ParseQuery<Vehicle> query = ParseQuery.getQuery(Vehicle.class);
        query.whereNotEqualTo("owner", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<Vehicle>() {
            @Override
            public void done(List<Vehicle> vehicles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting the vehicles",e);
                    return;
                }
                exploreAdapter.addAll(vehicles);
            }
        });

    }

    public void notifyAdapter() {
        exploreAdapter.notifyDataSetChanged();
    }
}