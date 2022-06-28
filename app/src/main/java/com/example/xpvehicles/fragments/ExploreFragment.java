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
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.List;

public class ExploreFragment extends Fragment {

    private static final String TAG = "Explore_Fragment";
    private ExploreAdapter exploreAdapter;
    private RecommendedVehiclesAdapter recommendedVehiclesAdapter;
    private MainActivity activity;
    private TextView tvNoAvailableRentVehicle;
    private FloatingActionButton fabAddVehicle;
    private SearchView searchView;

    public ExploreFragment(MainActivity mainActivity){
        activity = mainActivity;
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.clearFocus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_explore, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        bind();
        queryAllVehicles();
        bindExploreAdapter(view);
        bindRecommendedAdapter(view);
        setAddVehicleOnClickListener(fabAddVehicle);
        setSearchViewOnClickListener();
    }

    private void bind() {
        tvNoAvailableRentVehicle = activity.findViewById(R.id.tvNoAvailableRentVehicle);
        fabAddVehicle = activity.findViewById(R.id.fabAddVehicle);
        searchView = activity.findViewById(R.id.searchView);
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

    private void setSearchViewOnClickListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchQuery) {
                querySearchVehicles(searchQuery);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                if (searchQuery.isEmpty()){
                    queryAllVehicles();
                }
                else{
                    querySearchVehicles(searchQuery);
                }
                return false;
            }
        });
    }

    private void querySearchVehicles(String searchQuery) {
        ParseQuery<Vehicle> query = ParseQuery.getQuery(Vehicle.class);
        query.whereNotEqualTo("owner", ParseUser.getCurrentUser().getObjectId());
        query.whereFullText("name", searchQuery);
        query.findInBackground(new FindCallback<Vehicle>() {
            @Override
            public void done(List<Vehicle> vehicles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting the vehicles",e);
                    return;
                }
                exploreAdapter.clear();
                if (vehicles.size() > 0) {
//                    tvNoAvailableRentVehicle.setVisibility(View.GONE);
                    exploreAdapter.addAll(vehicles);
                } else {
//                    tvNoAvailableRentVehicle.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void queryAllVehicles() {
        ParseQuery<Vehicle> query = ParseQuery.getQuery(Vehicle.class);
        query.whereNotEqualTo("owner", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<Vehicle>() {
            @Override
            public void done(List<Vehicle> vehicles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting the vehicles",e);
                    return;
                }
                if (vehicles.size() > 0) {
                    tvNoAvailableRentVehicle.setVisibility(View.GONE);
                    exploreAdapter.addAll(vehicles);
                } else {
                    tvNoAvailableRentVehicle.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    public void notifyAdapter() {
        exploreAdapter.notifyDataSetChanged();
    }
}