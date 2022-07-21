package com.example.xpvehicles.interfaces;

import static com.example.xpvehicles.models.Vehicle.KEY_OWNER;
import static com.example.xpvehicles.models.Vehicle.KEY_VEHICLE_NAME;
import static java.lang.Double.parseDouble;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.adapters.ExploreAdapter;
import com.example.xpvehicles.fragments.ExploreFragment;
import com.example.xpvehicles.fragments.FilterDialogFragment;
import com.example.xpvehicles.models.Vehicle;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public interface SearchAndFilter {

    String TAG =  "SearchAndFilter";

    default void querySearchAndFilterVehicles(FilterDialogFragment fragment, ExploreAdapter exploreAdapter, String searchQuery, ParseGeoPoint userLocationGeoPoint,
                                              String maxDistance, String minPrice, String maxPrice, TextView tvNoAvailableRentVehicle) {
        ParseQuery<Vehicle> query = ParseQuery.getQuery(Vehicle.class);
        query.whereNotEqualTo(KEY_OWNER, ParseUser.getCurrentUser().getObjectId());

        // add query for max price
        if (!maxPrice.isEmpty()) {
            query.whereLessThanOrEqualTo("dailyPrice", parseDouble(maxPrice));
        }
        // add query for min price
        if (!minPrice.isEmpty()) {
            query.whereGreaterThanOrEqualTo("dailyPrice", parseDouble(minPrice));
        }
        // add query for max distance
        if (!maxDistance.isEmpty()){
            query.whereWithinMiles("geoLocation", userLocationGeoPoint, parseDouble(maxDistance));
        }
        if (searchQuery != null) {
            query.whereMatches(KEY_VEHICLE_NAME, searchQuery, "i");
        }

        query.findInBackground(new FindCallback<Vehicle>() {
            @Override
            public void done(List<Vehicle> vehicles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with filtering vehicles",e);
                    return;
                }
                exploreAdapter.setVehicles(vehicles, tvNoAvailableRentVehicle);

                if (fragment != null) {
                    fragment.dismiss();
                }
            }
        });
    }
}
