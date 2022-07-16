package com.example.xpvehicles.miscellaneous;

import androidx.annotation.NonNull;

public enum RentingStatus {
    PENDING_APPROVAL ("Pending Approval"),
    WAITING_DROP_OFF ("Approved: Waiting for renter to drop off"),
    READY_FOR_PICKUP ("Approved: Ready for pickup"),
    DENIED ("Denied"),
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