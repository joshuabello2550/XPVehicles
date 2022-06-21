package com.example.xpvehicles.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.xpvehicles.R;
import com.example.xpvehicles.models.Vehicle;

import java.util.List;

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesAdapter.ViewHolder> {

    private List<Vehicle> mVehicles;
    private TextView tvVehicleName;
    private TextView tvDistanceFromUser;
    private ImageView ivVehicle;
    private Context context;

    public VehiclesAdapter(List<Vehicle> vehicles){
        mVehicles = vehicles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View vehicleView =  inflater.inflate(R.layout.vehicle_card, parent, false);

        ViewHolder viewHolder = new ViewHolder(vehicleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehicle vehicle = mVehicles.get(position);

        tvVehicleName.setText(vehicle.getVehicleName());
        Glide.with(context).load(vehicle.getVehicleImage()).into(ivVehicle);
    }

    @Override
    public int getItemCount() {
        return mVehicles.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            super(itemView);
            tvVehicleName = itemView.findViewById(R.id.tvVehicleName);
            tvDistanceFromUser = itemView.findViewById(R.id.tvDistanceFromUser);
            ivVehicle = itemView.findViewById(R.id.ivVehicle);
        }
    }

    public void addAll(List allVehicles) {
        mVehicles.addAll(allVehicles);
        notifyDataSetChanged();
    }

}
