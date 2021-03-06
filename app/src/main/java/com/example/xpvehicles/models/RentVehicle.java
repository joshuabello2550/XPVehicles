package com.example.xpvehicles.models;


import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("RentVehicle")
public class RentVehicle extends ParseObject {

    public static final String KEY_VEHICLE=  "vehicle";
    public static final String KEY_RENTEE =  "rentee";
    public static final String KEY_PICK_UP_DATE =  "pickUpDate";
    public static final String KEY_RETURN_DATE =  "returnDate";
    public static final String KEY_STATUS =  "status";
    public static final String KEY_STORAGE_CENTER = "storageCenter";

    public Vehicle getVehicle() {
        return (Vehicle) getParseObject(KEY_VEHICLE);
    }

    public void setVehicle(Vehicle vehicle) {
        put(KEY_VEHICLE, vehicle);
    }

    public _User getRentee() {
        return (_User) getParseUser(KEY_RENTEE);
    }

    public void setRentee(_User rentee) {
        put(KEY_RENTEE, rentee);
    }

    public Date getPickUpDate() {
        return getDate(KEY_PICK_UP_DATE);
    }

    public void setPickUpDate(Date pickUpDate) {
        put(KEY_PICK_UP_DATE, pickUpDate);
    }

    public Date getReturnDate() {
        return getDate(KEY_RETURN_DATE);
    }

    public void setReturnDate(Date returnDate) {
        put(KEY_RETURN_DATE, returnDate);
    }

    public String getStatus() {
        return getString(KEY_STATUS);
    }

    public void setStatus(String status) {
        put(KEY_STATUS, status);
    }

    public StorageCenter getStorageCenter() {
        return (StorageCenter) getParseObject(KEY_STORAGE_CENTER);
    }

    public void setStorageCenter(StorageCenter storageCenter) {
        put(KEY_STORAGE_CENTER, storageCenter);
    }
}
