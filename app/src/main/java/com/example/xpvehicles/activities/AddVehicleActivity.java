package com.example.xpvehicles.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.asynchttpclient.callback.TextHttpResponseHandler;
import com.example.xpvehicles.R;
import com.example.xpvehicles.models.Vehicle;
import com.google.android.material.appbar.MaterialToolbar;
import com.parse.Parse;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class AddVehicleActivity extends AppCompatActivity {

    private static final String TAG = "Add_Vehicle_Activity";
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
        setTopAppBarOnClickListener();
        setAddVehicleOnClickListener();
    }

    private void bind() {
        topAppBar = findViewById(R.id.topAppBar);
        edtVehicleName = findViewById(R.id.edtvehicleName);
        edtDescription = findViewById(R.id.edtDescription);
        edtStreetAddress = findViewById(R.id.edtStreetAddress);
        edtCity = findViewById(R.id.edtCity);
        edtState = findViewById(R.id.edtState);
        edtZipCode = findViewById(R.id.edtZipCode);
        edtDailyPrice = findViewById(R.id.edtDailyPrice);
        btnAddVehicle = findViewById(R.id.btnAddVehicle);
    }

    private void setTopAppBarOnClickListener() {
        topAppBar.setNavigationOnClickListener(v -> {
            this.finish();
        });
    }

    private void setAddVehicleOnClickListener() {
        btnAddVehicle.setOnClickListener(v -> {
            String vehicleName = edtVehicleName.getText().toString();
            String description = edtDescription.getText().toString();
            String streetAddress = edtStreetAddress.getText().toString();
            String city = edtCity.getText().toString();
            String state = edtState.getText().toString();
            String zipCode = edtZipCode.getText().toString();
//            Double dailyPrice = Double.valueOf(edtDailyPrice.getText().toString());
            ParseUser owner = ParseUser.getCurrentUser();


            Vehicle vehicle = new Vehicle();
            vehicle.setOwner(owner);
            vehicle.setVehicleName(vehicleName);
            vehicle.setDescription(description);
//            vehicle.setDailyPrice(dailyPrice);
            getGeocodedLocation(streetAddress, city, state);
        });

    }

    private void getGeocodedLocation(String streetAddress, String city, String state) {
        String base_url = "https://maps.googleapis.com/maps/api/geocode/json?";
        String address = streetAddress + " " + city + " " + state;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", R.string.API_KEY);
        params.put("address", address);
        client.get(base_url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject test = jsonObject.getJSONObject("results");
                } catch (JSONException e) {
                    Log.e(TAG, "hit json exception when getting the latitude and longitude");
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Error sending the JSON request", throwable);
            }
        });


    }


}