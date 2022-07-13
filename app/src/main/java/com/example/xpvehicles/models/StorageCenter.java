package com.example.xpvehicles.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("StorageCenter")
public class StorageCenter extends ParseObject {

    public static final String KEY_AVAILABILITY=  "availability";
    public static final String KEY_STREET_ADDRESS =  "streetAddress";
    public static final String KEY_CITY =  "city";
    public static final String KEY_STATE =  "state";
    public static final String KEY_ZIPCODE =  "zipCode";
    public static final String KEY_IS_STORAGE_FULL = "isStorageFull";

    public Boolean getAvailability() {
        return getBoolean(KEY_AVAILABILITY);
    }

    public void setAvailability(Boolean availability) {
        put(KEY_AVAILABILITY, availability);
    }

    public String getStreetAddress() {
        return getString(KEY_STREET_ADDRESS);
    }

    public void setStreetAddress(String streetAddress) {
        put(KEY_STREET_ADDRESS, streetAddress);
    }

    public String getCity() {
        return getString(KEY_CITY);
    }

    public void setCity(String city) {
        put(KEY_CITY, city);
    }

    public String getState() {
        return getString(KEY_STATE);
    }

    public void setState(String state) {
        put(KEY_STATE, state);
    }

    public String getZipCode() {
        return getString(KEY_ZIPCODE);
    }

    public void setZipCode(String zipCode) {
        put(KEY_ZIPCODE, zipCode);
    }

    public Boolean getIsStorageFull() {
        return getBoolean(KEY_IS_STORAGE_FULL);
    }

    public void setIsStorageFull(Boolean isStorageFull) {
        put(KEY_IS_STORAGE_FULL, isStorageFull);
    }
}