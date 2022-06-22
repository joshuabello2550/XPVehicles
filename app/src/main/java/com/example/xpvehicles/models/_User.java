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


}
