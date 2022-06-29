package com.example.xpvehicles.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.example.xpvehicles.R;
import com.example.xpvehicles.fragments.ExploreFragment;
import com.example.xpvehicles.fragments.InboxFragment;
import com.example.xpvehicles.fragments.ProfileFragment;
import com.example.xpvehicles.fragments.SavedFragment;
import com.example.xpvehicles.fragments.RentingVehiclesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Main_Activity";
    public String userLocation;
    final ExploreFragment explore_fragment = new ExploreFragment(this);
    final InboxFragment inboxFragment = new InboxFragment(this);
    final SavedFragment saved_fragment = new SavedFragment(this);
    final RentingVehiclesFragment vehicles_fragment = new RentingVehiclesFragment(this);
    final ProfileFragment profile_fragment = new ProfileFragment(this);
    private BottomNavigationView bottomNavigationView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_navigation_search, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUserLocation();
        setContentView(R.layout.activity_main);
        bind();
        setBottomNavigationOnClick();
        setDefaultBottomNavigationSelection();
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
                        fragment = explore_fragment;
                        break;
                    case R.id.saved:
                        fragment = saved_fragment;
                        break;
                    case R.id.vehicles:
                        fragment = vehicles_fragment;
                        break;
                    case R.id.inbox:
                        fragment = inboxFragment;
                        break;
                    case R.id.profile:
                        fragment = profile_fragment;
                        break;
                    default:
                        fragment = explore_fragment;
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
        long UPDATE_INTERVAL = 1;  /* 10 secs */
        long MIN_DISTANCE = 1 * 1609; /* 1 mile */
        int REQUEST_CODE = 1;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL, MIN_DISTANCE, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Double userLongitude = location.getLongitude();
                Double userLatitude = location.getLatitude();
                userLocation = userLatitude + " " + String.valueOf(userLongitude);
                Log.i(TAG, "User's location is " + userLocation);
                explore_fragment.notifyAdapter();
            }
        });
    }

    public String getUserLocation() {
        return userLocation;
    }
}