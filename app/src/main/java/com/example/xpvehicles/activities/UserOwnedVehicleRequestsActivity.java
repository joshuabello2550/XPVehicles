package com.example.xpvehicles.activities;

import static com.example.xpvehicles.models.RentVehicle.KEY_VEHICLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.xpvehicles.R;
import com.example.xpvehicles.interfaces.IndicatorDots;
import com.example.xpvehicles.adapters.UserOwnedVehicleRequestsAdapter;
import com.example.xpvehicles.adapters.VehicleImagesAdapter;
import com.example.xpvehicles.interfaces.ParentActivity;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models.StorageCenter;
import com.example.xpvehicles.models.Vehicle;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UserOwnedVehicleRequestsActivity extends AppCompatActivity implements IndicatorDots, ParentActivity {

    private static final String TAG = "UserVehicleRequestsActivity";
    private static final String DAILY_PRICE_PREFIX = "Daily Price: $";
    private Vehicle vehicle;
    private MaterialToolbar topAppBar;
    private UserOwnedVehicleRequestsAdapter adapter;
    private TextView tvUserVehicleDetailsName;
    private TextView tvUserVehicleDetailsDailyPrice;
    private TextView tvNoRequests;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ImageButton ibEditVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vehicle = getIntent().getParcelableExtra("userVehicle");
        setContentView(R.layout.activity_user_owned_vehicle_requests);
        bind();
        queryUserOwnedVehicleRequests();
        bindUserOwnedVehicleRequestsAdapter();
        bindVehicleImagesAdapter(vehicle);
        setValues();
        setEditVehicleOnClickListener();
        setTopAppBarOnClickListener(topAppBar, this);
    }

    private void bind() {
        topAppBar = findViewById(R.id.userVehicleTopAppBar);
        tvUserVehicleDetailsName = findViewById(R.id.tvUserVehicleDetailsName);
        tvUserVehicleDetailsDailyPrice = findViewById(R.id.tvUserVehicleDetailsDailyPrice);
        tvNoRequests = findViewById(R.id.tvNoRequests);
        viewPager = findViewById(R.id.viewPagerUserVehicleDetails);
        tabLayout = findViewById(R.id.tabLayout);
        ibEditVehicle =  findViewById(R.id.ibEditVehicle);
    }

    private void bindUserOwnedVehicleRequestsAdapter() {
        List<RentVehicle> allVehicles = new ArrayList<>();
        adapter = new UserOwnedVehicleRequestsAdapter(allVehicles, this);

        RecyclerView rvVehicles = findViewById(R.id.rvRequests);
        rvVehicles.setAdapter(adapter);
        rvVehicles.setLayoutManager(new LinearLayoutManager(this));
    }

    private void bindVehicleImagesAdapter(Vehicle rentVehicle) {
        List<ParseFile> images = rentVehicle.getVehicleImages();
        VehicleImagesAdapter vehicleImagesAdapter = new VehicleImagesAdapter(this, images);
        viewPager.setAdapter(vehicleImagesAdapter);
        setViewPagerIndicatorDots(tabLayout, viewPager);
    }

    private void queryUserOwnedVehicleRequests() {
        ParseQuery<RentVehicle> query = ParseQuery.getQuery(RentVehicle.class);
        query.whereEqualTo(KEY_VEHICLE, vehicle);
        query.findInBackground(new FindCallback<RentVehicle>() {
            @Override
            public void done(List<RentVehicle> requests, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting the requests", e);
                    return;
                }
                adapter.setVehicles(requests, tvNoRequests);
            }
        });
    }

    private void setValues() {
        tvUserVehicleDetailsName.setText(vehicle.getVehicleName());
        tvUserVehicleDetailsDailyPrice.setText(DAILY_PRICE_PREFIX + vehicle.getDailyPrice());
    }

    private void setEditVehicleOnClickListener() {
        ibEditVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserOwnedVehicleRequestsActivity.this, EditVehicleActivity.class);
                intent.putExtra("vehicle", vehicle);
                startActivity(intent);
            }
        });
    }
}