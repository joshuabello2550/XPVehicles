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

import com.example.xpvehicles.R;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models.Vehicle;
import com.example.xpvehicles.models._User;
import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder>{

    public static final String TAG = "Requests_Adapter";
    private List<RentVehicle> mVehicles;

    public RequestsAdapter(List<RentVehicle> vehicles){
        mVehicles = vehicles;
    }

    @NonNull
    @Override
    public RequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View vehicleView = inflater.inflate(R.layout.request_card, parent, false);
        RequestsAdapter.ViewHolder viewHolder = new RequestsAdapter.ViewHolder(vehicleView);
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

        private RentVehicle vehicle;
        private Button btnRequestAccept;
        private Button btnRequestDeny;
        private TextView tvRequestDates;
        private TextView tvRequestName;
        private ImageView ivRequestProfileImage;

        public ViewHolder(View itemView) {
            super(itemView);
            bind(itemView);
            setAcceptOnClickListener();
            setDenyOnClickListener();
        }

        private void bind(View itemView) {
            btnRequestAccept = itemView.findViewById(R.id.btnRequestAccept);
            btnRequestDeny = itemView.findViewById(R.id.btnRequestDeny);
            tvRequestDates = itemView.findViewById(R.id.tvRequestDates);
            tvRequestName = itemView.findViewById(R.id.tvRequestName);
            ivRequestProfileImage = itemView.findViewById(R.id.ivRequestProfileImage);
        }

        public void setValues(RentVehicle vehicle) throws ParseException {
            this.vehicle = vehicle;
            // user requesting name
            _User userRequesting = (_User) vehicle.getRentee();
            String userRequestingName = userRequesting.fetchIfNeeded().getString("firstName") + " " + userRequesting.fetchIfNeeded().getString("lastName");
            tvRequestName.setText(userRequestingName);

            // request dates
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            // vehicle pickup date
            Date pickUpDate = vehicle.getPickUpDate();
            String formattedPickUpDate = sdf.format(pickUpDate);
            // vehicle return date
            Date returnDate = vehicle.getReturnDate();
            String formattedReturnDate = sdf.format(returnDate);
            String requestDates = formattedPickUpDate + " - " + formattedReturnDate;
            tvRequestDates.setText(requestDates);
        }

        private void setAcceptOnClickListener() {
            btnRequestAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vehicle.setStatus("approved");
                    vehicle.saveInBackground();
                }
            });
        }

        private void setDenyOnClickListener() {
            btnRequestDeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vehicle.setStatus("denied");
                    vehicle.saveInBackground();
                }
            });
        }
    }


}
