package com.example.xpvehicles.interfaces;

import android.graphics.Color;
import android.util.Log;

import com.example.xpvehicles.miscellaneous.RentingStatus;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public interface OrderInformation {

     String TAG = "OrderInformation";

    default int getStatusColor(RentingStatus status) {
        int statusColor;
        switch (status) {
            case PENDING_APPROVAL:
                statusColor = Color.parseColor(String.valueOf(RentingStatus.PENDING_APPROVAL_COLOR));
                break;
            case APPROVED:
                statusColor = Color.parseColor(String.valueOf(RentingStatus.APPROVED_COLOR));
                break;
            case DENIED:
                statusColor = Color.parseColor(String.valueOf(RentingStatus.DENIED_COLOR));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }
        return statusColor;
    }

    default int getNumberOfDays(Date pickUpDate, Date returnDate) {
        // used to allow same day returns
        int conversionDifference = 1;
        long diff = returnDate.getTime() - pickUpDate.getTime();
        int numberOfRentDays = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + conversionDifference;
        return numberOfRentDays;
    }

    default double getOrderTotal(int numberOfRentDays, double dailyPrice) {
        double orderTotal = numberOfRentDays * dailyPrice;
        Log.i(TAG, String.valueOf(orderTotal));
        return orderTotal;
    }
}
