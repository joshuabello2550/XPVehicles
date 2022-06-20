package com.example.xpvehicles.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.xpvehicles.R;
import com.google.android.material.appbar.MaterialToolbar;

public class AddVehicleActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private EditText edtVehicleName;
    private EditText edtDescription;
    private EditText edtStreetAddress;
    private EditText edtCity;
    private EditText edtState;
    private EditText edtZipCode;
    private EditText edtDailyPrice;
    private Button btnAddVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        bind();
        topAppBarOnClickListener();
    }

    private void topAppBarOnClickListener() {
        topAppBar.setNavigationOnClickListener(v -> {
            this.finish();
        });
    }

    private void bind() {
        topAppBar= findViewById(R.id.topAppBar);
        edtVehicleName = findViewById(R.id.edtvehicleName);
        edtDescription = findViewById(R.id.edtDescription);
        edtStreetAddress = findViewById(R.id.edtStreetAddress);
        edtCity = findViewById(R.id.edtCity);
        edtState = findViewById(R.id.edtState);
        edtZipCode = findViewById(R.id.edtZipCode);
        edtDailyPrice = findViewById(R.id.edtDailyPrice);
        btnAddVehicle = findViewById(R.id.btnAddVehicle);
    }
}