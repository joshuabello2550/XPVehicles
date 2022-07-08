package com.example.xpvehicles.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.xpvehicles.Miscellaneous.RentingStatus;
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

public class RentingRequestsActivity extends AppCompatActivity {

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

    private void setTopAppBarOnClickListener() {
        topAppBarRentingRequest.setNavigationOnClickListener(v -> {
            this.finish();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renting_requests);
        rentVehicle = getIntent().getParcelableExtra("rentVehicle");
        originalVehicle = (Vehicle) rentVehicle.getVehicle();
        bind();
        setTopAppBarOnClickListener();
        try {
            bindVehicleImagesAdapter();
            setValues();
        } catch (ParseException e) {
            Log.e(TAG, "error setting the values for RentingRequestsActivity");
        }
    }

    private void bindVehicleImagesAdapter() throws ParseException {
        List<ParseFile> images = originalVehicle.getVehicleImages();
        ViewPager2 viewPager = findViewById(R.id.viewPagerRentingRequestVehicleImages);
        VehicleImagesAdapter vehicleImagesAdapter = new VehicleImagesAdapter(this, images);
        viewPager.setAdapter(vehicleImagesAdapter);
        setVehicleSwipeListener(viewPager, images.size());
    }

    private void setVehicleSwipeListener(ViewPager2 viewPager, int totalNumberOfImages) {
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                TextView tvVehicleImagePosition = findViewById(R.id.tvVehicleImagePosition);
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
        tvOrderSummaryDailyPrice =  findViewById(R.id.tvOrderSummaryDailyPrice);
        tvOrderSummaryNumberOfDays =  findViewById(R.id.tvOrderSummaryNumberOfDays);
        tvOrderSummaryOrderTotal =  findViewById(R.id.tvOrderSummaryOrderTotal);
        tvRentingRequestsPickUpDate =  findViewById(R.id.tvRentingRequestsPickUpDate);
        tvRentingRequestReturnDate =  findViewById(R.id.tvRentingRequestReturnDate);
        tvRentingRequestStatus =  findViewById(R.id.tvRentingRequestStatus);
    }

    private void setValues() throws ParseException {
        // vehicle name
        Vehicle originalVehicle = (Vehicle) rentVehicle.getVehicle();
        String vehicleName = originalVehicle.fetchIfNeeded().getString("name");
        tvRentingRequestVehicleName.setText(vehicleName);

        // daily price
        Number dailyPrice = originalVehicle.fetchIfNeeded().getNumber("dailyPrice");
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

        //set order summary values
        calculateNumberOfDays(pickUpDate, returnDate);

        // status
        String status = rentVehicle.getStatus();
        RentingStatus rentingStatus = RentingStatus.valueOf(status);
        tvRentingRequestStatus.setText(rentingStatus.toString());
        setStatusColor(rentingStatus);
    }

    private void calculateNumberOfDays(Date pickUpDate, Date returnDate) {
        if (pickUpDate != null && returnDate != null) {
            int conversionDifference = 1;
            int conversionFactor = (1000 * 60 * 60 * 24);
            int numberOfRentDays = (int) ((returnDate.getTime() - pickUpDate.getTime()) / (conversionFactor)) + conversionDifference;
            tvOrderSummaryNumberOfDays.setText(String.valueOf(numberOfRentDays));
            calculateOrderTotal(numberOfRentDays);
        }
    }

    private void calculateOrderTotal(int numberOfRentDays) {
        int orderTotal = numberOfRentDays * (int) originalVehicle.getDailyPrice();
        Log.i(TAG, String.valueOf(orderTotal));
        tvOrderSummaryOrderTotal.setText("$" + orderTotal);
    }

    private void setStatusColor(RentingStatus status) {
        int statusColor;
        switch (status) {
            case PENDING_APPROVAL:
                statusColor = Color.parseColor(String.valueOf(RentingStatus.PENDING_APPROVAL_COLOR));
                break;
            case APPROVED:
                statusColor = Color.parseColor(String.valueOf(RentingStatus.APPROVED_COLOR));
                break;
            case DENIED:
                statusColor = Color.parseColor(String.valueOf(RentingStatus.DENIED_COLOR));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }
        tvRentingRequestStatus.setBackgroundColor(statusColor);
    }
}