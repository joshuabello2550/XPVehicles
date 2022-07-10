package com.example.xpvehicles.miscellaneous;

import androidx.annotation.NonNull;

public enum RentingStatus {
    PENDING_APPROVAL ("pending approval"),
    APPROVED ("approved"),
    DENIED ("denied"),
    PENDING_APPROVAL_COLOR ("#FFC107"),
    APPROVED_COLOR ("#4CAF50"),
    DENIED_COLOR ("#F44336");

    private String theStatus;

    RentingStatus(String s) {
        theStatus =  s;
    }

    @NonNull
    @Override
    public String toString() {
        return theStatus;
    }
}