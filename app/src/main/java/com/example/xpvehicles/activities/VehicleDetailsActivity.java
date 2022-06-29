package com.example.xpvehicles.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xpvehicles.R;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models.Vehicle;
import com.example.xpvehicles.models._User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class VehicleDetailsActivity extends AppCompatActivity {

    public static final String TAG = "VehicleDetailsActivity";
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
    private TextInputLayout detailsPickupDateOTF;
    private TextInputLayout detailsReturnDateOTF;
    private Date pickupDate;
    private Date returnDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vehicle = (Vehicle) getIntent().getParcelableExtra("vehicle");
        setContentView(R.layout.activity_vehicle_details);
        bind();
        setValues();
        setTopAppBarOnClickListener();
        setPickupDateOnClickListener();
        setReturnDateOnClickListener();
        setReserveNowOnClickListener();
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
        topAppBar = findViewById(R.id.filterTopAppBar);
        tvDetailsVehicleName = findViewById(R.id.tvDetailsVehicleName);
        ivDetailsVehicleImage = findViewById(R.id.ivDetailsVehicleImage);
        tvDetailsVehicleDescription = findViewById(R.id.tvDetailsVehicleDescription);
        tvDetailsDailyPrice = findViewById(R.id.tvDetailsDailyPrice);
        tvDetailsDistanceFromUser = findViewById(R.id.tvDetailsDistanceFromUser);
        tvOrderSummaryDailyPrice = findViewById(R.id.tvOrderSummaryDailyPrice);
        tvOrderSummaryNumberOfDays = findViewById(R.id.tvOrderSummaryNumberOfDays);
        tvOrderSummaryOrderTotal = findViewById(R.id.tvOrderSummaryOrderTotal);
        detailsPickupDateOTF = findViewById(R.id.detailsPickupDateOTF);
        detailsReturnDateOTF = findViewById(R.id.detailsReturnDateOTF);
    }

    private void setValues() {
        String distanceFromUser = vehicle.getDistanceFromUser();
        tvDetailsVehicleName.setText(vehicle.getVehicleName());
        tvDetailsVehicleDescription.setText(vehicle.getDescription());
        tvDetailsDailyPrice.setText("$" + vehicle.getDailyPrice() + "/day");
        tvDetailsDistanceFromUser.setText(distanceFromUser);
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
                        pickupDate = sdf.parse(formattedDate);
                    } catch (ParseException e) {
                        Log.e(TAG, "error converting the pickup date into a date object",e);
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
                        returnDate = sdf.parse(formattedDate);
                    } catch (ParseException e) {
                        Log.e(TAG, "error converting the return date into a date object",e);
                    }
                    calculateNumberOfDays();
                }
            });
        });
    }

    private void calculateNumberOfDays() {
        if (pickupDate != null && returnDate != null) {
            int conversionFactor = (1000 * 60 * 60 * 24);
            int numberOfRentDays = (int) ((returnDate.getTime() - pickupDate.getTime()) / (conversionFactor));
            tvOrderSummaryNumberOfDays.setText(String.valueOf(numberOfRentDays));
            calculateOrderTotal(numberOfRentDays);
        }
    }

    private void calculateOrderTotal(int numberOfRentDays) {
        Number orderTotal = numberOfRentDays * (int) vehicle.getDailyPrice();
        Log.i(TAG, String.valueOf(orderTotal));
        tvOrderSummaryOrderTotal.setText("$" + orderTotal);
    }

    private Boolean checkValidDates() {
        Boolean validDates = true;
        if (pickupDate == null) {
            detailsPickupDateOTF.setError("Enter a pickup date");
            validDates = false;
        }
        if (returnDate == null) {
            detailsReturnDateOTF.setError("Enter a return date");
            validDates = false;
        }
        return validDates;
    }


    private void setReserveNowOnClickListener() {
        btnReserveNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean validDates = checkValidDates();
                if (validDates) {
                    _User currentUser = (_User) ParseUser.getCurrentUser();
                    RentVehicle rentVehicle = createRentVehicle(currentUser);
                    requestVehicle(currentUser, rentVehicle);
                    finish();
                }
            }
        });
    }

    private RentVehicle createRentVehicle(_User currentUser) {
        final String INITIAL_VEHICLE_REQUEST_STATUS = "pending approval";

        RentVehicle rentVehicle = new RentVehicle();
        rentVehicle.setVehicle(vehicle);
        rentVehicle.setRentee(currentUser);
        rentVehicle.setPickUpDate(pickupDate);
        rentVehicle.setReturnDate(returnDate);
        rentVehicle.setStatus(INITIAL_VEHICLE_REQUEST_STATUS);
        rentVehicle.saveInBackground();
        return rentVehicle;
    }

    private void requestVehicle(_User currentUser, RentVehicle rentVehicle) {
        List<RentVehicle> rentedVehicles = currentUser.getRentedVehicles();
        rentedVehicles.add(rentVehicle);
        currentUser.setRentedVehicles(rentedVehicles);
        currentUser.saveInBackground();
    }
}