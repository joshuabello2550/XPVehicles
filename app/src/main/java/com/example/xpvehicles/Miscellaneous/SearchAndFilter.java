package com.example.xpvehicles.miscellaneous;

import static com.example.xpvehicles.models.Vehicle.KEY_DAILY_PRICE;
import static com.example.xpvehicles.models.Vehicle.KEY_GEO_LOCATION;
import static com.example.xpvehicles.models.Vehicle.KEY_OWNER;
import static com.example.xpvehicles.models.Vehicle.KEY_VEHICLE_NAME;
import static java.lang.Double.parseDouble;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.adapters.ExploreAdapter;
import com.example.xpvehicles.fragments.FilterDialogFragment;
import com.example.xpvehicles.models.Vehicle;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchAndFilter extends DialogFragment {

    private static final String TAG = "SearchAndFilter";
    private static final String sortByDistance = "Distance";
    private static String searchQuery;
    private static String minPrice;
    private static String maxPrice;
    private static String maxDistance;
    private static String sortBy;
    private static ParseGeoPoint userLocationGeoPoint;

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public static String getSearchQuery() {
        return searchQuery;
    }

    public void setMaxDistance(String maxDistance) {
        this.maxDistance = maxDistance;
    }

    public static String getMaxDistance() {
        return maxDistance;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public static String getMinPrice() {
        return minPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public static String getMaxPrice() {
        return maxPrice;
    }

    public void setUserLocationGeoPoint(ParseGeoPoint userLocationGeoPoint) {
        this.userLocationGeoPoint = userLocationGeoPoint;
    }

    public void setSortBy(String sortBy) {
        this.sortBy =  sortBy;
    }

    public static String getSortBy() {
        return sortBy;
    }

    public static ParseGeoPoint getUserLocationGeoPoint() {
        return userLocationGeoPoint;
    }

    public void querySearchAndFilterVehicles(FilterDialogFragment fragment, ExploreAdapter exploreAdapter, TextView tvNoAvailableRentVehicle) {
        ParseQuery<Vehicle> query = ParseQuery.getQuery(Vehicle.class);
        query.whereNotEqualTo(KEY_OWNER, ParseUser.getCurrentUser().getObjectId());

        addQuerySearchQuery(query);
        addQueryDistance(query);
        addQueryPrice(query);
        addQuerySortBy(query);

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

    private void addQuerySearchQuery(ParseQuery<Vehicle> query) {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            query.whereMatches(KEY_VEHICLE_NAME, searchQuery, "i");
        }
    }

    private void addQueryDistance(ParseQuery<Vehicle> query) {
        if (maxDistance != null && !maxDistance.isEmpty()){
            query.whereWithinMiles(KEY_GEO_LOCATION, userLocationGeoPoint, parseDouble(maxDistance));
        }
    }

    private void addQueryPrice(ParseQuery<Vehicle> query) {
        if (minPrice != null && !minPrice.isEmpty()) {
            query.whereGreaterThanOrEqualTo(KEY_DAILY_PRICE, parseDouble(minPrice));
        }
        if (maxPrice != null && !maxPrice.isEmpty()) {
            query.whereLessThanOrEqualTo(KEY_DAILY_PRICE, parseDouble(maxPrice));
        }
    }

    private void addQuerySortBy(ParseQuery<Vehicle> query) {
        if (sortBy == sortByDistance) {
            query.addAscendingOrder(KEY_DAILY_PRICE);
        }
    }
}
