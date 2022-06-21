package com.example.xpvehicles.models;

import androidx.core.app.NavUtils;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Vehicle")
public class Vehicle extends ParseObject {

    //TODO: finish vehicle

    public static final String KEY_VEHICLE_NAME =  "vehicleName";
    public static final String KEY_DESCRIPTION =  "description";
    public static final String KEY_VEHICLE_IMAGE =  "image";
    public static final String KEY_OWNER = "owner";
    public static final String KEY_DAILY_PRICE = "dailyPrice";
    public static final String KEY_GEO_LOCATION = "geoLocation";


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

    public ParseFile getVehicleImage(){
        return getParseFile("KEY_VEHICLE_IMAGE");
    }

    public void setVehicleImage(ParseFile image) {
        put(KEY_VEHICLE_IMAGE,image);
    }


}
