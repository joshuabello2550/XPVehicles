package com.example.xpvehicles.interfaces;

import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models.Vehicle;

import java.util.List;

public interface ParentAdapter {

    default void setVehicles(List<Vehicle> vehicles, List<Vehicle> addVehicles, TextView textViewNoVehicles, RecyclerView.Adapter adapter) {
        vehicles.clear();
        adapter.notifyDataSetChanged();
        if (addVehicles.size() > 0) {
            textViewNoVehicles.setVisibility(View.GONE);
            vehicles.addAll(addVehicles);
            adapter.notifyDataSetChanged();
        } else {
            textViewNoVehicles.setVisibility(View.VISIBLE);
        }
    }

    default void setRentVehicles(List<RentVehicle> vehicles, List<RentVehicle> addVehicles, TextView textViewNoVehicles, RecyclerView.Adapter adapter) {
        vehicles.clear();
        adapter.notifyDataSetChanged();
        if (addVehicles.size() > 0) {
            textViewNoVehicles.setVisibility(View.GONE);
            vehicles.addAll(addVehicles);
            adapter.notifyDataSetChanged();
        } else {
            textViewNoVehicles.setVisibility(View.VISIBLE);
        }
    }
}
