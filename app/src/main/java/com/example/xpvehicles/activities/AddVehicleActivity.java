package com.example.xpvehicles.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.xpvehicles.R;
import com.example.xpvehicles.models.Vehicle;
import com.google.android.material.appbar.MaterialToolbar;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
            String streetAddress = edtStreetAddress.getText().toString();
            String city = edtCity.getText().toString();
            String state = edtState.getText().toString();
            String zipCode = edtZipCode.getText().toString();
            getPlaceId(streetAddress, city, state);
        });
    }

    private void getPlaceId(String streetAddress, String city, String state) {
        String base_url = "https://maps.googleapis.com/maps/api/geocode/json?";
        String address = streetAddress + " " + city + " " + state;

        RequestParams params = new RequestParams();
        Resources res = this.getResources();
        params.put("key", res.getString(R.string.API_KEY));
        params.put("address", address);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(base_url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    String placeId = jsonObject.getJSONArray("results").getJSONObject(0).getString("place_id");
                    saveVehicle(placeId);
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

    private void saveVehicle(String placeId) {
        String vehicleName = edtVehicleName.getText().toString();
        String description = edtDescription.getText().toString();
//        Double dailyPrice = Double.valueOf(edtDailyPrice.getText().toString());
        ParseUser owner = ParseUser.getCurrentUser();

        Vehicle vehicle = new Vehicle();
        vehicle.setOwner(owner);
        vehicle.setVehicleName(vehicleName);
        vehicle.setDescription(description);
//            vehicle.setDailyPrice(dailyPrice);
        vehicle.setPlaceId(placeId);
        vehicle.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving the vehicle to parse", e);
                    return;
                }
                Log.i(TAG, "Post was successful");
                clearComponents();
            }
        });
    }

    private void clearComponents() {
        edtVehicleName.setText("");
        edtDescription.setText("");
        edtStreetAddress.setText("");
        edtCity.setText("");
        edtState.setText("");
        edtZipCode.setText("");
        // TODO: clear the daily price
    }
}