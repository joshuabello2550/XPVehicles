package com.example.xpvehicles.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.UserVehicleRequestsActivity;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models._User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.example.xpvehicles.models.Vehicle;
import com.example.xpvehicles.models._User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder>{

    public static final String TAG = "RequestsAdapter";
    private List<RentVehicle> vehicles;
    private UserVehicleRequestsActivity activity;
    private final String STATUS_APPROVED = "approved";
    private final String STATUS_PENDING_DENIED = "denied";

    public RequestsAdapter(List<RentVehicle> vehicles, UserVehicleRequestsActivity userVehicleRequestsActivity){
        this.vehicles = vehicles;
        activity = userVehicleRequestsActivity;
    }

    @NonNull
    @Override
    public RequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View vehicleView = inflater.inflate(R.layout.request_card, parent, false);
        RequestsAdapter.ViewHolder viewHolder = new RequestsAdapter.ViewHolder(vehicleView);
        return viewHolder;
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

    public void addAll(List<RentVehicle> allVehicles) {
        vehicles.addAll(allVehicles);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RentVehicle request;
        private Button btnRequestAccept;
        private Button btnRequestAccepted;
        private Button btnRequestDenied;
        private Button btnRequestDeny;
        private TextView tvRequestDates;
        private TextView tvRequestName;
        private ImageView ivRequestProfileImage;

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
            if (Objects.equals(request.getStatus(), STATUS_APPROVED)) {
                hideAcceptDenyButtons();
                btnRequestAccepted.setVisibility(View.VISIBLE);
            }
            else if (Objects.equals(request.getStatus(), STATUS_PENDING_DENIED)) {
                hideAcceptDenyButtons();
                btnRequestDenied.setVisibility(View.VISIBLE);
            }
        }

        private void setAcceptOnClickListener(View itemView) {
            btnRequestAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideAcceptDenyButtons();
                    btnRequestAccepted.setVisibility(View.VISIBLE);
                    request.setStatus(STATUS_APPROVED);
                    request.saveInBackground();
                }
            });
        }

        private void setDenyOnClickListener(View itemView) {
            btnRequestDeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideAcceptDenyButtons();
                    btnRequestDenied.setVisibility(View.VISIBLE);
                    request.setStatus(STATUS_PENDING_DENIED);
                    request.saveInBackground();
                }
            });
        }

        private void hideAcceptDenyButtons() {
            btnRequestDeny.setVisibility(View.GONE);
            btnRequestAccept.setVisibility(View.GONE);
        }
    }
}