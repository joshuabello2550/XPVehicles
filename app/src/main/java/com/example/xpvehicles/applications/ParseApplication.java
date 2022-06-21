package com.example.xpvehicles.applications;

import android.app.Application;

import com.example.xpvehicles.models.Vehicle;
import com.example.xpvehicles.models._User;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Register parse models
        ParseUser.registerSubclass(_User.class);
        ParseObject.registerSubclass(Vehicle.class);

        // Initializes Parse SDK as soon as the application is created
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("VSy9BgESdDvYNBhizAnwe8u4FQcvQpBTUSziFQ53")
                .clientKey("ZU2vfje4LiBUgZFaGQPNOhj4Xesv7c9PMdpbR8Et")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}