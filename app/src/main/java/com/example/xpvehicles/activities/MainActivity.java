package com.example.xpvehicles.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.xpvehicles.R;
import com.example.xpvehicles.fragments.ExploreFragment;
import com.example.xpvehicles.fragments.InboxFragment;
import com.example.xpvehicles.fragments.ProfileFragment;
import com.example.xpvehicles.fragments.SavedFragment;
import com.example.xpvehicles.fragments.RentingRequestsFragment;
import com.example.xpvehicles.models.Vehicle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseGeoPoint;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final ExploreFragment exploreFragment = new ExploreFragment();
    private final InboxFragment inboxFragment = new InboxFragment();
    private final SavedFragment savedFragment = new SavedFragment();
    private final RentingRequestsFragment vehiclesFragment = new RentingRequestsFragment();
    private final ProfileFragment profileFragment = new ProfileFragment();
    private static final long UPDATE_INTERVAL = 1;  /* 10 secs */
    private static final long MIN_DISTANCE = 1 * 1609; /* 1 mile */
    private static final int REQUEST_CODE = 1;
    private ParseGeoPoint userLocationGeoPoint;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserLocation();
        setContentView(R.layout.activity_main);
        bind();
        setBottomNavigationOnClick();
        setDefaultBottomNavigationSelection();
        pushNotification();
    }

    private void bind() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void setBottomNavigationOnClick() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.explore:
                        fragment = exploreFragment;
                        break;
                    case R.id.saved:
                        fragment = savedFragment;
                        break;
                    case R.id.vehicles:
                        fragment = vehiclesFragment;
                        break;
                    case R.id.inbox:
                        fragment = inboxFragment;
                        break;
                    case R.id.profile:
                        fragment = profileFragment;
                        break;
                    default:
                        fragment = exploreFragment;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                return true;
            }
        });
    }

    private void setDefaultBottomNavigationSelection(){
        bottomNavigationView.setSelectedItemId(R.id.explore);
    }

    private void setUserLocation() {
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL, MIN_DISTANCE, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Double userLongitude = location.getLongitude();
                Double userLatitude = location.getLatitude();
                userLocationGeoPoint = new ParseGeoPoint(userLatitude, userLongitude);
                Log.i(TAG, "User's location is " + userLocationGeoPoint);
                exploreFragment.notifyAdapter();
                savedFragment.notifyAdapter();
            }
        });
    }

    public ParseGeoPoint getUserLocationGeoPoint() {
        return userLocationGeoPoint;
    }

    public int getDistanceFromUser(Vehicle vehicle) {
        ParseGeoPoint vehicleGeoLocation = vehicle.getGeoLocation();
        ParseGeoPoint userGeoLocation = getUserLocationGeoPoint();
        int distanceFromUser = (int) vehicleGeoLocation.distanceInMilesTo(userGeoLocation);
        return distanceFromUser;
    }

    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    private void pushNotification() {
        HashMap<String, String> params = new HashMap<>();
        // Calling the cloud code function
        ParseCloud.callFunctionInBackground("pushNotificationNewRentRequest", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, com.parse.ParseException e) {
                if (e == null) {
                    // The function was executed, but it's interesting to check its response
                    alertDisplayer("Successful Push", "Check on your phone the notifications to confirm!");
                } else {
                    // Something went wrong
                    Log.e(TAG, "Error sending push notifications", e);
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "hello world");
    }
}
