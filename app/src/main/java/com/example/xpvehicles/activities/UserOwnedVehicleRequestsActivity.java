package com.example.xpvehicles.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.xpvehicles.R;
import com.example.xpvehicles.adapters.UserOwnedVehicleRequestsAdapter;
import com.example.xpvehicles.adapters.VehicleImagesAdapter;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models.Vehicle;
import com.google.android.material.appbar.MaterialToolbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UserOwnedVehicleRequestsActivity extends AppCompatActivity {

    public static final String TAG = "UserVehicleRequestsActivity";
    private Vehicle vehicle;
    private MaterialToolbar topAppBar;
    private UserOwnedVehicleRequestsAdapter adapter;
    private TextView tvUserVehicleDetailsName;
    private TextView tvUserVehicleDetailsDailyPrice;
    private TextView tvNoRequests;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vehicle = getIntent().getParcelableExtra("userVehicle");
        setContentView(R.layout.activity_user_vehicle_requests);
        bind();
        queryUserVehicles();
        bindAdapter();
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
        viewPager = findViewById(R.id.viewPagerUserVehicleDetails);
        tvNoRequests = findViewById(R.id.tvNoRequests);
    }

    private void bindAdapter() {
        List<RentVehicle> allVehicles = new ArrayList<>();
        adapter = new UserOwnedVehicleRequestsAdapter(allVehicles, this);

        RecyclerView rvVehicles = findViewById(R.id.rvRequests);
        rvVehicles.setAdapter(adapter);
        rvVehicles.setLayoutManager(new LinearLayoutManager(this));
    }

    private void bindVehicleImagesAdapter(Vehicle rentVehicle) {
        List<ParseFile> images = rentVehicle.getVehicleImages();
        VehicleImagesAdapter vehicleImagesAdapter =  new VehicleImagesAdapter(this, images);
        viewPager.setAdapter(vehicleImagesAdapter);
//            setVehicleSwipeListener(viewPager, images.size());
    }

    private void setValues() {
        final String DAILY_PRICE_PREFIX = "Daily Price: $";

        tvUserVehicleDetailsName.setText(vehicle.getVehicleName());
        tvUserVehicleDetailsDailyPrice.setText(DAILY_PRICE_PREFIX + vehicle.getDailyPrice());
        bindVehicleImagesAdapter(vehicle);
    }

    private void queryUserVehicles() {
        final String QUERY_PARAMETER_VEHICLE = "vehicle";

        ParseQuery<RentVehicle> query = ParseQuery.getQuery(RentVehicle.class);
        query.whereEqualTo(QUERY_PARAMETER_VEHICLE, vehicle);
        query.findInBackground(new FindCallback<RentVehicle>() {
            @Override
            public void done(List<RentVehicle> requests, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting the requests", e);
                    return;
                }
                adapter.clear();
                if (requests.size() > 0) {
                    tvNoRequests.setVisibility(View.GONE);
                    adapter.addAll(requests);
                } else {
                    tvNoRequests.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}