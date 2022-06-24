package com.example.xpvehicles.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.adapters.VehiclesAdapter;
import com.example.xpvehicles.models.Vehicle;
import com.example.xpvehicles.models._User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SavedFragment extends Fragment {

    private static final String TAG = "Saved_Fragment";
    private VehiclesAdapter adapter;

    public SavedFragment(MainActivity mainActivity) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_saved, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        bindAdapter(view);
        queryVehicles();
    }

    private void bindAdapter(View view) {
        List<Vehicle> allVehicles = new ArrayList<>();
        adapter = new VehiclesAdapter(this, allVehicles, (MainActivity)getActivity());

        RecyclerView rvVehicles = view.findViewById(R.id.rvVehicles);
        rvVehicles.setAdapter(adapter);
        rvVehicles.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
    }

    private void queryVehicles() {
        List<String> savedVehicles = ((_User) ParseUser.getCurrentUser()).getSavedVehicles();

        ParseQuery<Vehicle> query = ParseQuery.getQuery(Vehicle.class);
        query.whereContainedIn("objectId", savedVehicles);

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