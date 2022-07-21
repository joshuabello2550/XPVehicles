package com.example.xpvehicles.fragments;

import static java.lang.Integer.parseInt;

import android.app.Dialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.adapters.ExploreAdapter;
import com.example.xpvehicles.miscellaneous.SearchAndFilter;
import com.google.android.material.appbar.MaterialToolbar;
import com.parse.ParseGeoPoint;

public class FilterDialogFragment extends SearchAndFilter implements com.example.xpvehicles.interfaces.SearchAndFilter {

    private static final String TAG = "FilterDialogFragment";
    private ExploreAdapter exploreAdapter;
    private ExploreFragment exploreFragment;
    private MaterialToolbar filterTopAppBar;
    private TextView tvNoAvailableRentVehicle;
    private TextView tvClearAll;
    private EditText etMaxDistance;
    private EditText etMinPrice;
    private EditText etMaxPrice;
    private Button btnFilterShowVehicles;

    public FilterDialogFragment(ExploreFragment exploreFragment, ExploreAdapter exploreAdapter) {
        this.exploreAdapter = exploreAdapter;
        this.exploreFragment = exploreFragment;
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
        tvNoAvailableRentVehicle = getActivity().findViewById(R.id.tvNoAvailableRentVehicle);
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
                setMaxPrice(maxPrice);
                String minPrice = String.valueOf(etMinPrice.getText());
                setMinPrice(minPrice);
                String maxDistance = String.valueOf(etMaxDistance.getText());
                setMaxDistance(maxDistance);
                queryFilterVehicles();
            }
        });
    }

    private void queryFilterVehicles() {
        ParseGeoPoint userLocationGeoPoint = ((MainActivity) getActivity()).getUserLocationGeoPoint();
        setUserLocationGeoPoint(userLocationGeoPoint);
        querySearchAndFilterVehicles(this, exploreAdapter, tvNoAvailableRentVehicle);
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