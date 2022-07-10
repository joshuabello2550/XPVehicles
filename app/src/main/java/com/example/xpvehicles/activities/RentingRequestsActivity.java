package com.example.xpvehicles.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.xpvehicles.interfaces.ParentActivity;
import com.example.xpvehicles.interfaces.OrderInformation;
import com.example.xpvehicles.miscellaneous.RentingStatus;
import com.example.xpvehicles.R;
import com.example.xpvehicles.adapters.VehicleImagesAdapter;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models.Vehicle;
import com.google.android.material.appbar.MaterialToolbar;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RentingRequestsActivity extends AppCompatActivity implements ParentActivity, OrderInformation {

    private static final String TAG = "RentingRequestsActivity";
    private RentVehicle rentVehicle;
    private MaterialToolbar topAppBarRentingRequest;
    private Vehicle originalVehicle;
    private TextView tvRentingRequestVehicleName;
    private TextView tvRentingRequestVehicleDescription;
    private TextView tvRentingRequestDailyPrice;
    private TextView tvOrderSummaryDailyPrice;
    private TextView tvOrderSummaryNumberOfDays;
    private TextView tvOrderSummaryOrderTotal;
    private TextView tvRentingRequestsPickUpDate;
    private TextView tvRentingRequestReturnDate;
    private TextView tvRentingRequestStatus;
    private TextView tvVehicleImagePosition;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renting_requests);
        rentVehicle = getIntent().getParcelableExtra("rentVehicle");
        originalVehicle = (Vehicle) rentVehicle.getVehicle();
        bind();
        setTopAppBarOnClickListener(topAppBarRentingRequest, this);
        try {
            bindVehicleImagesAdapter();
            setValues();
        } catch (ParseException e) {
            Log.e(TAG, "error setting the values for RentingRequestsActivity");
        }
    }

    private void bindVehicleImagesAdapter() throws ParseException {
        List<ParseFile> images = originalVehicle.getVehicleImages();
        VehicleImagesAdapter vehicleImagesAdapter = new VehicleImagesAdapter(this, images);
        viewPager.setAdapter(vehicleImagesAdapter);
        setVehicleSwipeListener(viewPager, images.size());
    }

    private void setVehicleSwipeListener(ViewPager2 viewPager, int totalNumberOfImages) {
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                int currentPosition = position + 1;
                tvVehicleImagePosition.setText(currentPosition + " / " + totalNumberOfImages);
            }
        });
    }

    private void bind() {
        topAppBarRentingRequest = findViewById(R.id.topAppBarRentingRequest);
        tvRentingRequestVehicleName = findViewById(R.id.tvRentingRequestVehicleName);
        tvRentingRequestVehicleDescription = findViewById(R.id.tvRentingRequestVehicleDescription);
        tvRentingRequestDailyPrice = findViewById(R.id.tvRentingRequestDailyPrice);
        tvOrderSummaryDailyPrice = findViewById(R.id.tvOrderSummaryDailyPrice);
        tvOrderSummaryNumberOfDays = findViewById(R.id.tvOrderSummaryNumberOfDays);
        tvOrderSummaryOrderTotal = findViewById(R.id.tvOrderSummaryOrderTotal);
        tvRentingRequestsPickUpDate = findViewById(R.id.tvRentingRequestsPickUpDate);
        tvRentingRequestReturnDate = findViewById(R.id.tvRentingRequestReturnDate);
        tvRentingRequestStatus = findViewById(R.id.tvRentingRequestStatus);
        tvVehicleImagePosition = findViewById(R.id.tvVehicleImagePosition);
        viewPager = findViewById(R.id.viewPagerRentingRequestVehicleImages);
    }

    private void setValues() throws ParseException {
        // vehicle name
        Vehicle originalVehicle = (Vehicle) rentVehicle.getVehicle();
        String vehicleName = originalVehicle.fetchIfNeeded().getString("name");
        tvRentingRequestVehicleName.setText(vehicleName);

        // daily price
        Number dailyPrice =  originalVehicle.fetchIfNeeded().getNumber("dailyPrice");
        tvRentingRequestDailyPrice.setText("$" + dailyPrice);
        tvOrderSummaryDailyPrice.setText("$" + dailyPrice);

        // vehicle description
        String vehicleDescription = originalVehicle.fetchIfNeeded().getString("description");
        tvRentingRequestVehicleDescription.setText(vehicleDescription);

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd yyyy");
        // vehicle pickup date
        Date pickUpDate = rentVehicle.getPickUpDate();
        String formattedPickUpDate = sdf.format(pickUpDate);
        tvRentingRequestsPickUpDate.setText(formattedPickUpDate);
        // vehicle return date
        Date returnDate = rentVehicle.getReturnDate();
        String formattedReturnDate = sdf.format(returnDate);
        tvRentingRequestReturnDate.setText(formattedReturnDate);

        //set number of rent days
        int numberOfRentDays = getNumberOfDays(pickUpDate, returnDate);
        tvOrderSummaryNumberOfDays.setText(String.valueOf(numberOfRentDays));

        //set order total
        double orderTotal =  getOrderTotal(numberOfRentDays, (int) dailyPrice);
        tvOrderSummaryOrderTotal.setText("$" + orderTotal);

        // status
        String status = rentVehicle.getStatus();
        RentingStatus rentingStatus = RentingStatus.valueOf(status);
        tvRentingRequestStatus.setText(rentingStatus.toString());
        int statusColor = getStatusColor(rentingStatus);
        tvRentingRequestStatus.setBackgroundColor(statusColor);
    }
}