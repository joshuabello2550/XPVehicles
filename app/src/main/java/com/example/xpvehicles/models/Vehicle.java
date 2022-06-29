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

import okhttp3.Headers;

@ParseClassName("Vehicle")
public class Vehicle extends ParseObject {

    public static final String KEY_VEHICLE_NAME =  "name";
    public static final String KEY_DESCRIPTION =  "description";
    public static final String KEY_VEHICLE_IMAGE =  "image";
    public static final String KEY_OWNER = "owner";
    public static final String KEY_DAILY_PRICE = "dailyPrice";
    public static final String KEY_GEO_LOCATION = "geoLocation";
    public static final String KEY_PLACE_ID = "placeId";
    public static final String KEY_DISTANCE_FROM_USER = "distanceFromUser";

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

    public ParseFile getVehicleImage(){
        return getParseFile(KEY_VEHICLE_IMAGE);
    }

    public void setVehicleImage(ParseFile image) {
        put(KEY_VEHICLE_IMAGE,image);
    }

    public String getDistanceFromUser() {
        return getString(KEY_DISTANCE_FROM_USER);
    }

    public void setDistanceFromUser(String distanceFromUser) {
        put(KEY_DISTANCE_FROM_USER, distanceFromUser);
    }
}
