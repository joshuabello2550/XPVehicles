package com.example.xpvehicles.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xpvehicles.R;
import com.example.xpvehicles.models.Vehicle;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.parse.ParseFile;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class VehicleDetailsActivity extends AppCompatActivity {

    public static final String TAG = "Vehicle_Details_Activity";
    private Vehicle vehicle;
    private MaterialToolbar topAppBar;
    private EditText edtPickUpDate;
    private EditText edtReturnDate;
    private TextView tvDetailsVehicleName;
    private TextView tvDetailsVehicleDescription;
    private TextView tvDetailsDailyPrice;
    private TextView tvDetailsDistanceFromUser;
    private TextView tvOrderSummaryDailyPrice;
    private TextView tvOrderSummaryNumberOfDays;
    private TextView tvOrderSummaryOrderTotal;
    private ImageView ivDetailsVehicleImage;
    private Button btnReserveNow;
    private Date pickupDate;
    private Date returnDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vehicle = getIntent().getParcelableExtra("vehicle");
        setContentView(R.layout.activity_vehicle_details);
        bind();
        setStatusBarColor();
        setTopAppBarOnClickListener();
        setPickupDateOnClickListener();
        setReturnDateOnClickListener();
        setValues();
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.md_theme_light_surfaceVariant));
        }
    }

    private void setTopAppBarOnClickListener() {
        topAppBar.setNavigationOnClickListener(v -> {
            this.finish();
        });
    }

    private void bind() {
        btnReserveNow = findViewById(R.id.btnReserveNow);
        edtPickUpDate = findViewById(R.id.edtPickUpDate);
        edtReturnDate = findViewById(R.id.edtReturnDate);
        topAppBar = findViewById(R.id.topAppBar);
        tvDetailsVehicleName = findViewById(R.id.tvDetailsVehicleName);
        ivDetailsVehicleImage = findViewById(R.id.ivDetailsVehicleImage);
        tvDetailsVehicleDescription = findViewById(R.id.tvDetailsVehicleDescription);
        tvDetailsDailyPrice = findViewById(R.id.tvDetailsDailyPrice);
        tvDetailsDistanceFromUser = findViewById(R.id.tvDetailsDistanceFromUser);
        tvOrderSummaryDailyPrice = findViewById(R.id.tvOrderSummaryDailyPrice);
        tvOrderSummaryNumberOfDays = findViewById(R.id.tvOrderSummaryNumberOfDays);
        tvOrderSummaryOrderTotal = findViewById(R.id.tvOrderSummaryOrderTotal);

    }

    private void setValues() {
        tvDetailsVehicleName.setText(vehicle.getVehicleName());
        tvDetailsVehicleDescription.setText(vehicle.getDescription());
        tvDetailsDailyPrice.setText("$" + vehicle.getDailyPrice() + "/day");
        tvDetailsDistanceFromUser.setText(vehicle.getDistanceFromUser());
        tvOrderSummaryDailyPrice.setText("$" + vehicle.getDailyPrice());
        tvOrderSummaryNumberOfDays.setText("0");
        tvOrderSummaryOrderTotal.setText("$0");

        ParseFile image = vehicle.getVehicleImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivDetailsVehicleImage);
        }
    }

    private void setPickupDateOnClickListener() {
        edtPickUpDate.setOnClickListener(v -> {
            MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
            materialDateBuilder.setTitleText("PICKUP DATE");
            MaterialDatePicker materialDatePicker = materialDateBuilder.build();
            materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            // positive button == ok button
            materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override public void onPositiveButtonClick(Long selection) {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.setTimeInMillis(selection);
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    String formattedDate  = sdf.format(calendar.getTime());
                    edtPickUpDate.setText(formattedDate);
                    try {
                        // Converts the string date back into a date Object
                        returnDate = sdf.parse(formattedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    calculateNumberOfDays();
                }
            });
        });
    }

    private void setReturnDateOnClickListener() {
        edtReturnDate.setOnClickListener(v -> {
            MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
            materialDateBuilder.setTitleText("PICKUP DATE");
            MaterialDatePicker materialDatePicker = materialDateBuilder.build();
            materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            // positive button == ok button
            materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override public void onPositiveButtonClick(Long selection) {
                    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendar.setTimeInMillis(selection);
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    String formattedDate  = sdf.format(calendar.getTime());
                    edtReturnDate.setText(formattedDate);
                    try {
                        // Converts the string date back into a date Object
                        pickupDate = sdf.parse(formattedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    calculateNumberOfDays();
                }
            });
        });
    }

    private void calculateNumberOfDays() {
        if (pickupDate != null && returnDate != null) {
            int conversionFactor = (1000 * 60 * 60 * 24);
            int numberOfRentDays = (int) ((pickupDate.getTime() - returnDate.getTime()) / (conversionFactor));
            tvOrderSummaryNumberOfDays.setText(String.valueOf(numberOfRentDays));
            calculateOrderTotal(numberOfRentDays);
        }
    }

    private void calculateOrderTotal(int numberOfRentDays) {
        Number orderTotal = numberOfRentDays * (int) vehicle.getDailyPrice();
        Log.i(TAG, String.valueOf(orderTotal));
        tvOrderSummaryOrderTotal.setText("$" + orderTotal);
    }


}