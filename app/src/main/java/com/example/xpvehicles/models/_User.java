package com.example.xpvehicles.models;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.xpvehicles.activities.MainActivity;
import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("_User")
public class _User extends ParseUser {

    private static final String TAG = "_User";
    public static final String KEY_FIRST_NAME =  "firstName";
    public static final String KEY_LAST_NAME =  "lastName";
    private Location userLocation;

    public String getFirstName() {
        return getString(KEY_FIRST_NAME);
    }

    public void setFirstName(String firstName) {
        put(KEY_FIRST_NAME, firstName);
    }

    public String getLastName() {
        return getString(KEY_LAST_NAME);
    }

    public void setLastName(String lastName) {
        put(KEY_LAST_NAME, lastName);
    }

    private void userLocation() {
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
        long MIN_DISTANCE = 1 * 1609; /* 1 mile */
        int REQUEST_CODE = 1;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL, MIN_DISTANCE, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                userLocation = location;
                Log.i(TAG, "Location is " + location.getLongitude() + "," + location.getLatitude());
            }
        });
    }


}
