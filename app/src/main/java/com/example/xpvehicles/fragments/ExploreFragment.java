package com.example.xpvehicles.fragments;

import static com.example.xpvehicles.models.Vehicle.KEY_OWNER;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.AddVehicleActivity;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.adapters.ExploreAdapter;
import com.example.xpvehicles.adapters.RecommendedVehiclesAdapter;
import com.example.xpvehicles.miscellaneous.SearchAndFilter;
import com.example.xpvehicles.models.Vehicle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends SearchAndFilter {

    private static final String TAG = "ExploreFragment";
    private ExploreAdapter exploreAdapter;
    private RecommendedVehiclesAdapter recommendedVehiclesAdapter;
    private TextView tvNoAvailableRentVehicle;
    private FloatingActionButton fabAddVehicle;
    private SearchView searchView;
    private ImageView ivFilter;
    private View mainLayout;

    public ExploreFragment(){}

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
        bindExploreAdapter(view);
        querySearchVehicles();
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
        Animation expandIn = AnimationUtils.loadAnimation(getActivity(), R.anim.expand_in);
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
                setSearchQuery(searchQuery);
                querySearchVehicles();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                    setSearchQuery(searchQuery);
                    querySearchVehicles();
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

    private void querySearchVehicles() {
        ParseGeoPoint userLocationGeoPoint = ((MainActivity) getActivity()).getUserLocationGeoPoint();
        setUserLocationGeoPoint(userLocationGeoPoint);
        querySearchAndFilterVehicles(null, exploreAdapter, tvNoAvailableRentVehicle);
    }

    private void setFilterOnClickListener() {
        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterDialogFragment filterDialogFragment = new FilterDialogFragment(ExploreFragment.this, exploreAdapter);
                filterDialogFragment.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });
    }

    public void notifyAdapter() {
        exploreAdapter.notifyDataSetChanged();
    }
}