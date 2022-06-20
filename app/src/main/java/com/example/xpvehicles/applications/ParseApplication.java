package com.example.xpvehicles.applications;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    //Register parse models

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("VSy9BgESdDvYNBhizAnwe8u4FQcvQpBTUSziFQ53")
                .clientKey("ZU2vfje4LiBUgZFaGQPNOhj4Xesv7c9PMdpbR8Et")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}