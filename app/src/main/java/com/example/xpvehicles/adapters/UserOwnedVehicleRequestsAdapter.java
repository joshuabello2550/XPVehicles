package com.example.xpvehicles.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.xpvehicles.interfaces.OrderInformation;
import com.example.xpvehicles.interfaces.ParentAdapter;
import com.example.xpvehicles.miscellaneous.RentingStatus;
import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.UserOwnedVehicleRequestsActivity;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models.StorageCenter;
import com.example.xpvehicles.models._User;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class UserOwnedVehicleRequestsAdapter extends RecyclerView.Adapter<UserOwnedVehicleRequestsAdapter.ViewHolder> implements ParentAdapter, OrderInformation {

    private static final String TAG = "RequestsAdapter";
    private static final String QUERY_PARAMETER_STORAGE_CENTER_AVAILABILITY = "availability";
    private List<RentVehicle> vehicles;
    private UserOwnedVehicleRequestsActivity activity;

    public UserOwnedVehicleRequestsAdapter(List<RentVehicle> vehicles, UserOwnedVehicleRequestsActivity userOwnedVehicleRequestsActivity){
        this.vehicles = vehicles;
        activity = userOwnedVehicleRequestsActivity;
    }

    @NonNull
    @Override
    public UserOwnedVehicleRequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vehicleView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_owned_vehicle_request_card, parent, false);
        return new ViewHolder(vehicleView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RentVehicle vehicle = vehicles.get(position);
        try {
            holder.setValues(vehicle);
        } catch (ParseException e) {
            Log.e(TAG, "Error setting the list of requests for a vehicle",e);
        }
        holder.setInitialAcceptOrDenyStatus(vehicle);
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    public void setVehicles(List<RentVehicle> addVehicles, TextView textViewNoVehicles) {
        setRentVehicles(vehicles, addVehicles, textViewNoVehicles, this);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RentVehicle request;
        private Button btnRequestAccept;
        private Button btnRequestAccepted;
        private Button btnRequestDenied;
        private Button btnRequestDeny;
        private ImageButton ibExpandMore;
        private ImageButton ibExpandLess;
        private TextView tvRequestDates;
        private TextView tvRequestName;
        private TextView tvDropOffAddress;
        private TextView tvLockerCode;
        private ImageView ivRequestProfileImage;
        private ConstraintLayout constraintLayoutMoreInformationContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            bind(itemView);
            setAcceptOnClickListener(itemView);
            setDenyOnClickListener(itemView);
        }

        private void bind(View itemView) {
            btnRequestAccept = itemView.findViewById(R.id.btnRequestAccept);
            btnRequestDeny = itemView.findViewById(R.id.btnRequestDeny);
            tvRequestDates = itemView.findViewById(R.id.tvRequestDates);
            tvRequestName = itemView.findViewById(R.id.tvRequestName);
            ivRequestProfileImage = itemView.findViewById(R.id.ivRequestProfileImage);
            btnRequestAccepted = itemView.findViewById(R.id.btnRequestAccepted);
            btnRequestDenied = itemView.findViewById(R.id.btnRequestDenied);
            ibExpandMore =  itemView.findViewById(R.id.ibExpandMore);
            ibExpandLess =  itemView.findViewById(R.id.ibExpandLess);
            tvDropOffAddress = itemView.findViewById(R.id.tvDropOffAddress);
            tvLockerCode = itemView.findViewById(R.id.tvLockerCode);
            constraintLayoutMoreInformationContainer =  itemView.findViewById(R.id.constraintLayoutMoreInformationContainer);
        }

        private void setValues(RentVehicle vehicle) throws ParseException {
            request = vehicle;
            // user requesting name
            _User userRequesting = (_User) vehicle.getRentee();
            String userRequestingName = userRequesting.fetchIfNeeded().getString("firstName") + " " + userRequesting.fetchIfNeeded().getString("lastName");
            tvRequestName.setText(userRequestingName);

            // user requesting profile image
            ParseFile profileImage = userRequesting.getProfileImage();
            if (profileImage != null) {
                Glide.with(activity).load(profileImage.getUrl()).into(ivRequestProfileImage);
            }

            // request dates
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
            // vehicle pickup date
            Date pickUpDate = vehicle.getPickUpDate();
            String formattedPickUpDate = sdf.format(pickUpDate);
            // vehicle return date
            Date returnDate = vehicle.getReturnDate();
            String formattedReturnDate = sdf.format(returnDate);
            String requestDates = formattedPickUpDate + " - " + formattedReturnDate;
            tvRequestDates.setText(requestDates);
        }

        private void setInitialAcceptOrDenyStatus(RentVehicle request) {
            if (Objects.equals(request.getStatus(), RentingStatus.APPROVED.name())) {
                hideAcceptDenyButtons();
                displayAcceptedStatus();
            }
            else if (Objects.equals(request.getStatus(), RentingStatus.DENIED.name())) {
                hideAcceptDenyButtons();
                displayDeniedStatus();
            }
        }

        private void setAcceptOnClickListener(View itemView) {
            btnRequestAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideAcceptDenyButtons();
                    displayAcceptedStatus();
                    request.setStatus(RentingStatus.APPROVED.name());
                    request.saveInBackground();
                    setStorageCenter(request);
                }
            });
        }

        private void setDenyOnClickListener(View itemView) {
            btnRequestDeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideAcceptDenyButtons();
                    displayDeniedStatus();
                    request.setStatus(RentingStatus.DENIED.name());
                    request.saveInBackground();
                }
            });
        }

        private void setStorageCenter(RentVehicle rentVehicle) {
            ParseQuery<StorageCenter> query = ParseQuery.getQuery(StorageCenter.class);
            query.whereEqualTo(QUERY_PARAMETER_STORAGE_CENTER_AVAILABILITY, true);
            // find the first available storage center
            query.getFirstInBackground(new GetCallback<StorageCenter>() {
                @Override
                public void done(StorageCenter storageCenter, com.parse.ParseException e) {
                    rentVehicle.setStorageCenter(storageCenter);
                    rentVehicle.saveInBackground();
                    storageCenter.setAvailability(false);
                    storageCenter.saveInBackground();
                    setDropOffAddress(storageCenter);
                }
            });
        }

        private void setDropOffAddress(StorageCenter storageCenter) {
            String dropOffAddress = getStorageCenterAddress(storageCenter);
            tvDropOffAddress.setText(dropOffAddress);
            setLockerCode(storageCenter);
        }

        private void setLockerCode(StorageCenter storageCenter) {
            int lockerCode = getLockerCode(storageCenter);
            tvLockerCode.setText(String.valueOf(lockerCode));
        }

        private void hideAcceptDenyButtons() {
            btnRequestDeny.setVisibility(View.GONE);
            btnRequestAccept.setVisibility(View.GONE);
        }

        private void displayAcceptedStatus() {
            btnRequestAccepted.setVisibility(View.VISIBLE);
            ibExpandMore.setVisibility(View.VISIBLE);
            setExpandMoreOnClickListener();
            setExpandLessOnClickListener();
        }

        private void displayDeniedStatus() {
            btnRequestDenied.setVisibility(View.VISIBLE);
        }

        private void setExpandMoreOnClickListener() {
            ibExpandMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    constraintLayoutMoreInformationContainer.setVisibility(View.VISIBLE);
                    ibExpandMore.setVisibility(View.GONE);
                    ibExpandLess.setVisibility(View.VISIBLE);
                    StorageCenter storageCenter = null;
                    try {
                        storageCenter = request.getStorageCenter().fetchIfNeeded();
                    } catch (ParseException e) {
                        Log.e(TAG, "error getting the storage center", e);
                    }
                    setDropOffAddress(storageCenter);
                }
            });
        }

        private void setExpandLessOnClickListener() {
            ibExpandLess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    constraintLayoutMoreInformationContainer.setVisibility(View.GONE);
                    ibExpandLess.setVisibility(View.GONE);
                    ibExpandMore.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
