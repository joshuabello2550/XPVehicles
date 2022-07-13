package com.example.xpvehicles.models;

import androidx.core.app.NavUtils;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

import okhttp3.Headers;

@ParseClassName("Vehicle")
public class Vehicle extends ParseObject {
    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_VEHICLE_NAME =  "name";
    public static final String KEY_DESCRIPTION =  "description";
    public static final String KEY_OWNER = "owner";
    public static final String KEY_DAILY_PRICE = "dailyPrice";
    public static final String KEY_GEO_LOCATION = "geoLocation";
    public static final String KEY_PLACE_ID = "placeId";
    public static final String KEY_VEHICLE_IMAGES = "images";
    public static final String KEY_STREET_ADDRESS = "streetAddress";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "state";
    public static final String KEY_ZIP_CODE = "zipCode";

    public ParseUser getOwner() {
        return getParseUser(KEY_OWNER);
    }

    public void setOwner (ParseUser owner) {
        put(KEY_OWNER, owner);
    }

    public String getVehicleName() {
        return getString(KEY_VEHICLE_NAME);
    }

    public void setVehicleName(String vehicleName) {
        put(KEY_VEHICLE_NAME, vehicleName);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public Number getDailyPrice() {
        return getNumber(KEY_DAILY_PRICE);
    }

    public void setDailyPrice(Number dailyPrice) {
        put(KEY_DAILY_PRICE, dailyPrice);
    }

    public ParseGeoPoint getGeoLocation () {
        return getParseGeoPoint(KEY_GEO_LOCATION);
    }

    public void setGeoLocation(ParseGeoPoint geoLocation) {
        put(KEY_GEO_LOCATION, geoLocation);
    }

    public String getPlaceId () {
        return getString(KEY_PLACE_ID);
    }

    public void setPlaceId(String placeId) {
        put(KEY_PLACE_ID, placeId);
    }

    public List<ParseFile> getVehicleImages() {
        return getList(KEY_VEHICLE_IMAGES);
    }

    public void setVehicleImages(List<ParseFile> vehicleImages) {
        put(KEY_VEHICLE_IMAGES, vehicleImages);
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
        return getString(KEY_ZIP_CODE);
    }

    public void setZipCode(String zipCode) {
        put(KEY_ZIP_CODE, zipCode);
    }
}
