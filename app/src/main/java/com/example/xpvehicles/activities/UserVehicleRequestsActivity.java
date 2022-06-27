package com.example.xpvehicles.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xpvehicles.R;
import com.example.xpvehicles.adapters.RequestsAdapter;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models.Vehicle;
import com.example.xpvehicles.models._User;
import com.google.android.material.appbar.MaterialToolbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserVehicleRequestsActivity extends AppCompatActivity {

    public static final String TAG = "User_Vehicle_Requests_Activity";
    private Vehicle vehicle;
    private MaterialToolbar topAppBar;
    private TextView tvUserVehicleDetailsName;
    private TextView tvUserVehicleDetailsDailyPrice;
    private ImageView ivUserVehicleDetails;
    private RequestsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vehicle = getIntent().getParcelableExtra("userVehicle");
        setContentView(R.layout.activity_user_vehicle_requests);
        bind();
        bindAdapter();
        queryUserVehicles();
        setValues();
        setTopAppBarOnClickListener();
    }

    private void setTopAppBarOnClickListener() {
        topAppBar.setNavigationOnClickListener(v -> {
            this.finish();
        });
    }

    private void bind() {
        topAppBar = findViewById(R.id.userVehicleTopAppBar);
        tvUserVehicleDetailsName = findViewById(R.id.tvUserVehicleDetailsName);
        tvUserVehicleDetailsDailyPrice = findViewById(R.id.tvUserVehicleDetailsDailyPrice);
        ivUserVehicleDetails = findViewById(R.id.ivUserVehicleDetails);
    }

    private void bindAdapter() {
        List<RentVehicle> allVehicles = new ArrayList<>();
        adapter = new RequestsAdapter(allVehicles);

        RecyclerView rvVehicles = findViewById(R.id.rvRequests);
        rvVehicles.setAdapter(adapter);
        rvVehicles.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setValues() {
        tvUserVehicleDetailsName.setText(vehicle.getVehicleName());
        tvUserVehicleDetailsDailyPrice.setText("Daily Price: $" + vehicle.getDailyPrice());
        ParseFile image = vehicle.getVehicleImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivUserVehicleDetails);
        }
    }

    private void queryUserVehicles() {
        ParseQuery<RentVehicle> query = ParseQuery.getQuery(RentVehicle.class);
        query.whereEqualTo("vehicle", vehicle);
        query.findInBackground(new FindCallback<RentVehicle>() {
            @Override
            public void done(List<RentVehicle> requests, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting the requests", e);
                    return;
                }
                adapter.addAll(requests);
            }
        });
    }
}