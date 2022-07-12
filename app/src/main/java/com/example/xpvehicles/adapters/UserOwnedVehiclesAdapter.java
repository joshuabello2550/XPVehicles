package com.example.xpvehicles.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.xpvehicles.interfaces.IndicatorDots;
import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.activities.UserOwnedVehicleRequestsActivity;
import com.example.xpvehicles.interfaces.ParentAdapter;
import com.example.xpvehicles.models.Vehicle;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseFile;

import java.util.List;

public class UserOwnedVehiclesAdapter extends RecyclerView.Adapter<UserOwnedVehiclesAdapter.ViewHolder> implements IndicatorDots, ParentAdapter {

    private static final String TAG = "UserVehiclesAdapter";
    private List<Vehicle> vehicles;
    private Fragment fragment;
    private MainActivity activity;

    public UserOwnedVehiclesAdapter(Fragment fragment, List<Vehicle> vehicles, MainActivity activity){
        this.vehicles = vehicles;
        this.fragment = fragment;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vehicleView =  LayoutInflater.from(fragment.getContext()).inflate(R.layout.user_owned_vehicle_card, parent, false);
        return new ViewHolder(vehicleView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehicle vehicle = vehicles.get(position);
        holder.setValues(vehicle);
        holder.setViewHolderOnClickListener(holder, vehicle);
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }


    public void setVehicles(List<Vehicle> addVehicles, TextView textViewNoVehicles) {
        setVehicles(vehicles, addVehicles, textViewNoVehicles, this);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUserVehicleName;
        private ViewPager2 viewPager;
        private View viewUserOwnedVehicleDummyView;
        private TabLayout tabLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            bind(itemView);
        }

        private void bind(View itemView) {
            tvUserVehicleName = itemView.findViewById(R.id.tvUserVehicleName);
            viewPager = itemView.findViewById(R.id.viewPagerUserVehicleImage);
            viewUserOwnedVehicleDummyView = itemView.findViewById(R.id.viewUserOwnedVehicleDummyView);
            tabLayout =  itemView.findViewById(R.id.tabLayout);
        }

        public void setValues(Vehicle vehicle) {
            // Vehicle name
            tvUserVehicleName.setText(vehicle.getVehicleName());

            // Vehicle image
            bindVehicleImagesAdapter(vehicle);
        }

        private void bindVehicleImagesAdapter(Vehicle rentVehicle) {
            List<ParseFile> images = rentVehicle.getVehicleImages();
            VehicleImagesAdapter vehicleImagesAdapter =  new VehicleImagesAdapter(activity, images);
            viewPager.setAdapter(vehicleImagesAdapter);
            setViewPagerIndicatorDots(tabLayout, viewPager);
        }

        private void setViewHolderOnClickListener(ViewHolder holder, Vehicle vehicle) {
            viewUserOwnedVehicleDummyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, UserOwnedVehicleRequestsActivity.class);
                    i.putExtra("userVehicle", vehicle);
                    fragment.startActivity(i);
                }
            });
        }
    }
}
