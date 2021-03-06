package com.example.xpvehicles.applications;

import android.app.Application;

import com.example.xpvehicles.models.Locker;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models.StorageCenter;
import com.example.xpvehicles.models.Vehicle;
import com.example.xpvehicles.models._User;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

public class ParseApplication extends Application {

    private static final String PARSE_APPLICATION_ID = "VSy9BgESdDvYNBhizAnwe8u4FQcvQpBTUSziFQ53";
    private static final String PARSE_CLIENT_KEY = "ZU2vfje4LiBUgZFaGQPNOhj4Xesv7c9PMdpbR8Et";
    private static final String PARSE_SERVER_HTTP_ADDRESS = "https://parseapi.back4app.com";
    private static final String FIREBASE_SENDER_ID = "612946493259";

    @Override
    public void onCreate() {
        super.onCreate();
        //Register parse models
        ParseUser.registerSubclass(_User.class);
        ParseObject.registerSubclass(Vehicle.class);
        ParseObject.registerSubclass(RentVehicle.class);
        ParseObject.registerSubclass(StorageCenter.class);
        ParseObject.registerSubclass(Locker.class);

        // Initializes Parse SDK as soon as the application is created
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(PARSE_APPLICATION_ID)
                .clientKey(PARSE_CLIENT_KEY)
                .server(PARSE_SERVER_HTTP_ADDRESS)
                .build()
        );

        ArrayList<String> channels = new ArrayList<>();

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", FIREBASE_SENDER_ID);
        installation.put("channels", channels);
        if (ParseUser.getCurrentUser() != null) {
            installation.put("user", ParseUser.getCurrentUser().getObjectId());
        }
        installation.setDeviceToken(installation.getDeviceToken());
        installation.saveInBackground();
    }
}