package com.example.xpvehicles.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("_User")
public class _User extends ParseUser {

    public static final String KEY_FIRST_NAME =  "firstName";
    public static final String KEY_LAST_NAME =  "lastName";


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
