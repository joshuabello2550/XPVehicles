package com.example.xpvehicles.adapters;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.activities.VehicleDetailsActivity;
import com.example.xpvehicles.fragments.ExploreFragment;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models.Vehicle;
import com.example.xpvehicles.models._User;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Headers;

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

        }

    }

}
