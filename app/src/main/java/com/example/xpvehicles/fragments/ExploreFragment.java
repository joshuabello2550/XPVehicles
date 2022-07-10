package com.example.xpvehicles.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ExploreFragment extends Fragment {

    private static final String TAG = "ExploreFragment";
    private static final String QUERY_PARAMETER_OWNER = "owner";
    private static final String QUERY_PARAMETER_NAME = "name";
    private ExploreAdapter exploreAdapter;
    private RecommendedVehiclesAdapter recommendedVehiclesAdapter;
    private MainActivity activity;
    private TextView tvNoAvailableRentVehicle;
    private FloatingActionButton fabAddVehicle;
    private SearchView searchView;
    private ImageView ivFilter;
    private View mainLayout;

    public ExploreFragment() {}

    public ExploreFragment(MainActivity mainActivity){
        activity = mainActivity;
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setQuery("", false);
        searchView.clearFocus();
        mainLayout.requestFocus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        bind(view);
        queryAllVehicles();
        bindExploreAdapter(view);
        bindRecommendedAdapter(view);
        setAddVehicleOnClickListener(fabAddVehicle);
        setSearchViewOnClickListener();
        setFilterOnClickListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tvNoAvailableRentVehicle.setVisibility(View.GONE);
    }

    private void bind(View view) {
        mainLayout =  view.findViewById(R.id.mainLayout);
        tvNoAvailableRentVehicle = view.findViewById(R.id.tvNoAvailableRentVehicle);
        fabAddVehicle = view.findViewById(R.id.fabAddVehicle);
        searchView = view.findViewById(R.id.searchView);
        ivFilter = view.findViewById(R.id.ivFilter);
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
        Animation expandIn = AnimationUtils.loadAnimation(activity, R.anim.expand_in);
        fabAddVehicle.startAnimation(expandIn);
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

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewCollapsed();
            }
        });
    }

    private void querySearchVehicles(String searchQuery) {
        ParseQuery<Vehicle> parseQuery = ParseQuery.getQuery(Vehicle.class);
        parseQuery.whereNotEqualTo(QUERY_PARAMETER_OWNER, ParseUser.getCurrentUser().getObjectId());
        parseQuery.whereMatches(QUERY_PARAMETER_NAME, searchQuery, "i");

        // Fetches the vehicles that start with the searchQuery or include the searchQuery within the name
        parseQuery.findInBackground(new FindCallback<Vehicle>() {
            @Override
            public void done(List<Vehicle> vehicles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting the vehicles",e);
                    return;
                }
                exploreAdapter.setVehicles(vehicles, tvNoAvailableRentVehicle);
            }
        });
    }

    private void queryAllVehicles() {
        ParseQuery<Vehicle> query = ParseQuery.getQuery(Vehicle.class);
        query.whereNotEqualTo(QUERY_PARAMETER_OWNER, ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<Vehicle>() {
            @Override
            public void done(List<Vehicle> vehicles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting the vehicles",e);
                    return;
                }
                exploreAdapter.setVehicles(vehicles, tvNoAvailableRentVehicle);
            }
        });
    }

    private void setFilterOnClickListener() {
        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterDialogFragment filterDialogFragment = new FilterDialogFragment(exploreAdapter, activity);
                filterDialogFragment.show(activity.getSupportFragmentManager(), TAG);
            }
        });
    }

    public void notifyAdapter() {
        exploreAdapter.notifyDataSetChanged();
    }
}