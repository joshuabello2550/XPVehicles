package com.example.xpvehicles.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.xpvehicles.interfaces.IndicatorDots;
import com.example.xpvehicles.interfaces.ParentAdapter;
import com.example.xpvehicles.interfaces.OrderInformation;
import com.example.xpvehicles.miscellaneous.RentingStatus;
import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.activities.RentingRequestsActivity;
import com.example.xpvehicles.fragments.RentingRequestsFragment;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models.Vehicle;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RentingRequestsAdapter extends RecyclerView.Adapter<RentingRequestsAdapter.ViewHolder> implements IndicatorDots, ParentAdapter, OrderInformation {

    public static final String TAG = "RentingVehiclesAdapter";
    private List<RentVehicle> vehicles;
    private RentingRequestsFragment fragment;
    private MainActivity activity;
    private FrameLayout frameLayout;

    public RentingRequestsAdapter(RentingRequestsFragment fragment, List<RentVehicle> vehicles, MainActivity activity) {
        this.vehicles = vehicles;
        this.fragment = fragment;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        frameLayout =  activity.findViewById(R.id.flProgressContainer);
        View vehicleView = LayoutInflater.from(fragment.getContext()).inflate(R.layout.renting_request_vehicle_card, parent, false);
        return new ViewHolder(vehicleView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RentVehicle vehicle = vehicles.get(position);
        frameLayout.setVisibility(View.GONE);
        try {
            holder.setValues(vehicle);
        } catch (ParseException e) {
            Log.e(TAG, "Error setting the values for the vehicles the user is renting", e);
        }
        holder.setCardOnClickListener(vehicle);
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    public void setVehicles(List<RentVehicle> addVehicles, TextView textViewNoVehicles) {
        setRentVehicles(vehicles, addVehicles, textViewNoVehicles, this);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRentingVehicleName;
        private TextView tvRentingPickUpDate;
        private TextView tvRentingReturnDate;
        private TextView tvStatus;
        private ViewPager2 viewPager;
        private TabLayout tabLayout;
        private MaterialCardView rentingRequestsVehicleCard;

        public ViewHolder(View itemView) {
            super(itemView);
            bind(itemView);
        }

        private void bind(View itemView) {
            tvRentingVehicleName = itemView.findViewById(R.id.tvRentingVehicleName);
            tvRentingPickUpDate = itemView.findViewById(R.id.tvRentingPickUpDate);
            tvRentingReturnDate = itemView.findViewById(R.id.tvRentingReturnDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            viewPager = itemView.findViewById(R.id.viewPagerRentingVehicleImages);
            tabLayout = itemView.findViewById(R.id.tabLayout);
            rentingRequestsVehicleCard =  itemView.findViewById(R.id.rentingRequestsVehicleCard);
        }

        public void setValues(RentVehicle rentVehicle) throws ParseException {
            // Vehicle name
            Vehicle originalVehicle = (Vehicle) rentVehicle.getVehicle();
            String vehicleName = originalVehicle.fetchIfNeeded().getString("name");
            tvRentingVehicleName.setText(vehicleName);

            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd yyyy");
            // vehicle pickup date
            Date pickUpDate = rentVehicle.getPickUpDate();
            String formattedPickUpDate = sdf.format(pickUpDate);
            tvRentingPickUpDate.setText(formattedPickUpDate);
            // vehicle return date
            Date returnDate = rentVehicle.getReturnDate();
            String formattedReturnDate = sdf.format(returnDate);
            tvRentingReturnDate.setText(formattedReturnDate);

            // status
            String status = rentVehicle.getStatus();
            RentingStatus rentingStatus = RentingStatus.valueOf(status);
            tvStatus.setText(rentingStatus.toString());
            int statusColor = getStatusColor(rentingStatus);
            tvStatus.setBackgroundColor(statusColor);

            // vehicle image
            bindVehicleImagesAdapter(originalVehicle);
        }

        private void bindVehicleImagesAdapter(Vehicle rentVehicle) {
            List<ParseFile> images = rentVehicle.getVehicleImages();
            VehicleImagesAdapter vehicleImagesAdapter = new VehicleImagesAdapter(activity, images);
            viewPager.setAdapter(vehicleImagesAdapter);
            setViewPagerIndicatorDots(tabLayout, viewPager);
        }

        private void setCardOnClickListener(RentVehicle vehicle) {
            rentingRequestsVehicleCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, RentingRequestsActivity.class);
                    intent.putExtra("rentVehicle", vehicle);
                    activity.startActivity(intent);
                }
            });
        }
    }
}