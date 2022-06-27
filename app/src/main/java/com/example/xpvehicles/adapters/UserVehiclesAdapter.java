package com.example.xpvehicles.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.activities.UserVehicleRequestsActivity;
import com.example.xpvehicles.models.Vehicle;
import com.parse.ParseFile;

import java.util.List;

public class UserVehiclesAdapter extends RecyclerView.Adapter<UserVehiclesAdapter.ViewHolder> {

    public static final String TAG = "Vehicles_Adapter";
    private List<Vehicle> mVehicles;
    private Fragment fragment;
    private MainActivity activity;

    public UserVehiclesAdapter(Fragment fragment, List<Vehicle> vehicles, MainActivity activity){
        mVehicles = vehicles;
        this.fragment = fragment;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragment.getContext());

        // Inflate the custom layout
        View vehicleView = inflater.inflate(R.layout.user_vehicle_card, parent, false);
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

    public void addAll(List<Vehicle> allVehicles) {
        mVehicles.addAll(allVehicles);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivUserVehicle;
        private TextView tvUserVehicleName;

        public ViewHolder(View itemView) {
            super(itemView);
            bind(itemView);
            //add
        }

        private void bind(View itemView) {
            ivUserVehicle = itemView.findViewById(R.id.ivUserVehicle);
            tvUserVehicleName = itemView.findViewById(R.id.tvUserVehicleName);
        }

        public void setValues(Vehicle vehicle) {
            // Vehicle name
            tvUserVehicleName.setText(vehicle.getVehicleName());

            // Vehicle image
            ParseFile image = vehicle.getVehicleImage();
            if (image != null) {
                Glide.with(fragment.getContext()).load(image.getUrl()).into(ivUserVehicle);
            }
            setUserVehicleImageOnClickListener(vehicle);
        }

        private void setUserVehicleImageOnClickListener(Vehicle vehicle) {
            ivUserVehicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity, UserVehicleRequestsActivity.class);
                    i.putExtra("userVehicle", vehicle);
                    fragment.startActivity(i);
                }
            });
        }

    }
}
