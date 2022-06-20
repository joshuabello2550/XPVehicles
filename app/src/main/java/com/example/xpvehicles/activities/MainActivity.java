package com.example.xpvehicles.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.xpvehicles.R;
import com.example.xpvehicles.fragments.ExploreFragment;
import com.example.xpvehicles.fragments.InboxFragment;
import com.example.xpvehicles.fragments.ProfileFragment;
import com.example.xpvehicles.fragments.SavedFragment;
import com.example.xpvehicles.fragments.VehiclesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    final Fragment explore_fragment = new ExploreFragment();
    final Fragment inbox_fragment = new InboxFragment();
    final Fragment saved_fragment = new SavedFragment();
    final Fragment vehicles_fragment = new VehiclesFragment();
    final Fragment profile_fragment = new ProfileFragment();
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind();
        bottomNavigationOnClick();
    }

    private void bind() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void bottomNavigationOnClick() {
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