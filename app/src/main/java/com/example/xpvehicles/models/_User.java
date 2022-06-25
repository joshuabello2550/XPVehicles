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

import java.util.ArrayList;
import java.util.List;

@ParseClassName("_User")
public class _User extends ParseUser {

    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_SAVED_VEHICLES = "savedVehicles";
    public static final String KEY_RENTED_VEHICLES = "rentedVehicles";

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

    public List<String> getSavedVehicles() {
        List<String> savedVehicles = getList(KEY_SAVED_VEHICLES);
        if (savedVehicles == null) {
            savedVehicles = new ArrayList<>();
        }
        return savedVehicles;
    }

    public void setSavedVehicles(List<String> listSavedVehicles) {
        put(KEY_SAVED_VEHICLES, listSavedVehicles);
    }

    public List<RentVehicle> getRentedVehicles() {
        List<RentVehicle> rentedVehicles = getList(KEY_RENTED_VEHICLES);
        if (rentedVehicles == null) {
            rentedVehicles = new ArrayList<>();
        }
        return rentedVehicles;
    }

    public void setRentedVehicles(List<RentVehicle> listRentedVehicles) {
        put(KEY_RENTED_VEHICLES, listRentedVehicles);
    }

}
