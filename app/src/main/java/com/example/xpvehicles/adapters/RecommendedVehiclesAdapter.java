package com.example.xpvehicles.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.activities.VehicleDetailsActivity;
import com.example.xpvehicles.fragments.ExploreFragment;
import com.example.xpvehicles.fragments.RentingVehiclesFragment;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models.Vehicle;
import com.example.xpvehicles.models._User;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Headers;

public class RecommendedVehiclesAdapter extends RecyclerView.Adapter<RecommendedVehiclesAdapter.ViewHolder> {

    public static final String TAG = "RecommendedVehiclesAdapter";
    private List<Vehicle> mVehicles;
    private ExploreFragment fragment;
    private MainActivity activity;

    public RecommendedVehiclesAdapter(ExploreFragment fragment, List<Vehicle> vehicles, MainActivity activity){
        mVehicles = vehicles;
        this.fragment = fragment;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragment.getContext());
        View vehicleView =  inflater.inflate(R.layout.recommend_vehicle_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(vehicleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehicle vehicle = mVehicles.get(position);
        holder.setValues(vehicle);
    }

    @Override
    public int getItemCount() {
        return mVehicles.size();
    }

    public void addAll(List<Vehicle> recommendedVehicles) {
        mVehicles.addAll(recommendedVehicles);
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
