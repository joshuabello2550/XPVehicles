package com.example.xpvehicles.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.example.xpvehicles.R;
import com.example.xpvehicles.fragments.ExploreFragment;
import com.example.xpvehicles.fragments.InboxFragment;
import com.example.xpvehicles.fragments.ProfileFragment;
import com.example.xpvehicles.fragments.SavedFragment;
import com.example.xpvehicles.fragments.VehiclesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Main_Activity";
    final Fragment explore_fragment = new ExploreFragment();
    final Fragment inbox_fragment = new InboxFragment();
    final Fragment saved_fragment = new SavedFragment();
    final Fragment vehicles_fragment = new VehiclesFragment();
    final Fragment profile_fragment = new ProfileFragment();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind();
        setBottomNavigationOnClick();
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
                        fragment = inbox_fragment;
                        break;
                    case R.id.vehicles:
                        fragment = saved_fragment;
                        break;
                    case R.id.inbox:
                        fragment = vehicles_fragment;
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
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.explore);
    }


}