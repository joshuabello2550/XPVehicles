package com.example.xpvehicles.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.fragments.ExploreFragment;
import com.example.xpvehicles.models.Vehicle;

import java.util.List;

public class RecommendedVehiclesAdapter extends RecyclerView.Adapter<RecommendedVehiclesAdapter.ViewHolder> {

    public static final String TAG = "RecommendedVehiclesAdapter";
    private List<Vehicle> vehicles;
    private ExploreFragment fragment;
    private MainActivity activity;

    public RecommendedVehiclesAdapter(ExploreFragment fragment, List<Vehicle> vehicles, MainActivity activity){
        this.vehicles = vehicles;
        this.fragment = fragment;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vehicleView =  LayoutInflater.from(fragment.getContext()).inflate(R.layout.recommend_vehicle_card, parent, false);
        return new ViewHolder(vehicleView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehicle vehicle = vehicles.get(position);
        holder.setValues(vehicle);
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    public void addAll(List<Vehicle> recommendedVehicles) {
        vehicles.addAll(recommendedVehicles);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
            bind(itemView);
        }

        private void bind(View itemView) {
        }

        public void setValues(Vehicle vehicle){
        }
    }
}

