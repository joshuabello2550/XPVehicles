package com.example.xpvehicles.fragments;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.adapters.ExploreAdapter;
import com.example.xpvehicles.models.Vehicle;
import com.google.android.material.appbar.MaterialToolbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class FilterDialogFragment extends DialogFragment {

    private static final String TAG = "FilterDialogFragment";
    private static final String QUERY_PARAMETER_OWNER = "owner";
    private ExploreAdapter exploreAdapter;
    private MainActivity activity;
    private MaterialToolbar filterTopAppBar;
    private TextView tvNoAvailableRentVehicle;
    private TextView tvClearAll;
    private EditText etMaxDistance;
    private EditText etMinPrice;
    private EditText etMaxPrice;
    private Button btnFilterShowVehicles;

    public FilterDialogFragment(ExploreAdapter exploreAdapter, MainActivity activity) {
        this.exploreAdapter = exploreAdapter;
        this.activity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // fragment slides in and down to exit
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        bind(view);
        setTopAppBarOnClickListener();
        setShowVehiclesOnClickListener();
        setClearAllOnCLickListener();
    }

    private void bind(View view) {
        filterTopAppBar = view.findViewById(R.id.filterTopAppBar);
        tvNoAvailableRentVehicle = activity.findViewById(R.id.tvNoAvailableRentVehicle);
        etMaxDistance = view.findViewById(R.id.etMaxDistance);
        etMinPrice = view.findViewById(R.id.etMinPrice);
        etMaxPrice = view.findViewById(R.id.etMaxPrice);
        btnFilterShowVehicles = view.findViewById(R.id.btnFilterShowVehicles);
        tvClearAll = view.findViewById(R.id.tvClearAll);
    }

    private void setTopAppBarOnClickListener() {
        filterTopAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void setShowVehiclesOnClickListener() {
        btnFilterShowVehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maxPrice = String.valueOf(etMaxPrice.getText());
                String minPrice = String.valueOf(etMinPrice.getText());
                String maxDistance = String.valueOf(etMaxDistance.getText());
                queryFilterVehicles(maxDistance, minPrice, maxPrice);
            }
        });
    }

    private void queryFilterVehicles(String maxDistance, String minPrice, String maxPrice) {
        ParseQuery<Vehicle> query = ParseQuery.getQuery(Vehicle.class);
        query.whereNotEqualTo(QUERY_PARAMETER_OWNER, ParseUser.getCurrentUser().getObjectId());

        // add query for max price
        if (!maxPrice.isEmpty()) {
            query.whereLessThanOrEqualTo("dailyPrice", parseDouble(maxPrice));
        }
        // add query for min price
        if (!minPrice.isEmpty()) {
            query.whereGreaterThanOrEqualTo("dailyPrice", parseDouble(minPrice));
        }
        // add query for max distance
        if (!maxDistance.isEmpty()){
            query.whereWithinMiles("geoLocation", activity.getUserLocationGeoPoint(), parseDouble(maxDistance));
        }

        query.findInBackground(new FindCallback<Vehicle>() {
            @Override
            public void done(List<Vehicle> vehicles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with filtering vehicles",e);
                    return;
                }
                exploreAdapter.setVehicles(vehicles, tvNoAvailableRentVehicle);
                dismiss();
            }
        });
    }

    private void setClearAllOnCLickListener() {
        tvClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMaxDistance.setText("");
                etMinPrice.setText("");
                etMaxPrice.setText("");
            }
        });
    }
}