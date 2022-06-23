package com.example.xpvehicles.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xpvehicles.R;
import com.example.xpvehicles.models.Vehicle;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.parse.ParseFile;

import org.w3c.dom.Text;

public class VehicleDetailsActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private EditText edtPickUpDate;
    private EditText edtReturnDate;
    private TextView tvDetailsVehicleName;
    private TextView tvDetailsVehicleDescription;
    private TextView tvDetailsDailyPrice;
    private TextView tvDetailsDistanceFromUser;
    private ImageView ivDetailsVehicleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);
        bind();
        setTopAppBarOnClickListener();
        setPickupDateOnClickListener();
        setReturnDateOnClickListener();
        setValues();
    }

    private void setTopAppBarOnClickListener() {
        topAppBar.setNavigationOnClickListener(v -> {
            this.finish();
        });
    }

    private void bind() {
        edtPickUpDate = findViewById(R.id.edtPickUpDate);
        edtReturnDate = findViewById(R.id.edtReturnDate);
        topAppBar = findViewById(R.id.topAppBar);
        tvDetailsVehicleName = findViewById(R.id.tvDetailsVehicleName);
        ivDetailsVehicleImage = findViewById(R.id.ivDetailsVehicleImage);
        tvDetailsVehicleDescription = findViewById(R.id.tvDetailsVehicleDescription);
        tvDetailsDailyPrice = findViewById(R.id.tvDetailsDailyPrice);
        tvDetailsDistanceFromUser = findViewById(R.id.tvDetailsDistanceFromUser);
    }

    private void setValues() {
        Vehicle vehicle = getIntent().getParcelableExtra("vehicle");
        tvDetailsVehicleName.setText(vehicle.getVehicleName());
        tvDetailsVehicleDescription.setText(vehicle.getDescription());
        tvDetailsDailyPrice.setText("$" + vehicle.getDailyPrice() + "/day");
        tvDetailsDistanceFromUser.setText(vehicle.getDistanceFromUser());
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
            materialDatePicker.addOnPositiveButtonClickListener(
                    selection -> edtPickUpDate.setText(materialDatePicker.getHeaderText()));
        });
    }

    private void setReturnDateOnClickListener() {
        edtReturnDate.setOnClickListener(v -> {
            MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
            materialDateBuilder.setTitleText("PICKUP DATE");
            MaterialDatePicker materialDatePicker = materialDateBuilder.build();
            materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            // positive button == ok button
            materialDatePicker.addOnPositiveButtonClickListener(
                    selection -> edtReturnDate.setText(materialDatePicker.getHeaderText()));
        });
    }


}