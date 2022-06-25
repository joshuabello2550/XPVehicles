package com.example.xpvehicles.adapters;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
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
import com.example.xpvehicles.fragments.RentingVehiclesFragment;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models.Vehicle;
import com.example.xpvehicles.models._User;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Headers;

public class RentingVehiclesAdapter extends RecyclerView.Adapter<RentingVehiclesAdapter.ViewHolder> {

    public static final String TAG = "Vehicles_Adapter";
    private List<RentVehicle> mVehicles;
    private RentingVehiclesFragment fragment;
    private MainActivity activity;

    public RentingVehiclesAdapter(RentingVehiclesFragment fragment, List<RentVehicle> vehicles, MainActivity activity){
        mVehicles = vehicles;
        this.fragment = fragment;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragment.getContext());

        // Inflate the custom layout
        View vehicleView =  inflater.inflate(R.layout.renting_vehicles_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(vehicleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RentVehicle vehicle = mVehicles.get(position);
        try {
            holder.setValues(vehicle);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mVehicles.size();
    }

    public void addAll(List<RentVehicle> allVehicles) {
        mVehicles.addAll(allVehicles);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRentingVehicleName;
        private TextView tvRentingPickUpDate;
        private TextView tvRentingReturnDate;
        private TextView tvStatus;
        private ImageView ivRentingVehicleImage;

        public ViewHolder(View itemView) {
            super(itemView);
            bind(itemView);
        }

        private void bind(View itemView) {
            tvRentingVehicleName = itemView.findViewById(R.id.tvRentingVehicleName);
            tvRentingPickUpDate = itemView.findViewById(R.id.tvRentingPickUpDate);
            tvRentingReturnDate = itemView.findViewById(R.id.tvRentingReturnDate);
            ivRentingVehicleImage = itemView.findViewById(R.id.ivRentingVehicleImage);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }

        public void setValues(RentVehicle rentvehicle) throws ParseException{
            // Vehicle name
            Vehicle originalVehicle = (Vehicle) rentvehicle.getVehicle();
            String vehicleName = originalVehicle.fetchIfNeeded().getString("name");
            tvRentingVehicleName.setText(vehicleName);

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            // vehicle pickup date
            Date pickUpDate = rentvehicle.getPickUpDate();
            String formattedPickUpDate = sdf.format(pickUpDate);
            tvRentingPickUpDate.setText(formattedPickUpDate);
            // vehicle return date
            Date returnDate = rentvehicle.getReturnDate();
            String formattedReturnDate = sdf.format(returnDate);
            tvRentingReturnDate.setText(formattedReturnDate);

            // status
            String status = rentvehicle.getStatus();
            tvStatus.setText(status);
            setStatusColor(status);

            // vehicle image
            ParseFile image = originalVehicle.getVehicleImage();
            if (image != null) {
                Glide.with(fragment.getContext()).load(image.getUrl()).into(ivRentingVehicleImage);
            }
        }

        private void setStatusColor(String status) {
            int statusColor;
            switch (status) {
                case "pending approval":
                    statusColor = Color.parseColor("#FFC107");
                    break;
                case "approved":
                    statusColor = Color.parseColor("#4CAF50");
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + status);
            }
            tvStatus.setBackgroundColor(statusColor);
        }
    }


}
