package com.example.xpvehicles.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.LoginActivity;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.adapters.ExploreAdapter;
import com.example.xpvehicles.adapters.UserVehiclesAdapter;
import com.example.xpvehicles.models.Vehicle;
import com.example.xpvehicles.models._User;
import com.google.android.material.appbar.MaterialToolbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ProfileFragment extends Fragment {

    private static final String TAG = "Profile_Fragment";
    private UserVehiclesAdapter adapter;
    private MaterialToolbar profileTopAppBar;
    private TextView tvProfileUserName;
    private ImageView ivSetProfileImage;

    public ProfileFragment(MainActivity mainActivity) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        bind(view);
        bindAdapter(view);
        queryUserVehicles();
        setValues();
        setTopAppBarOnClickListener();
    }

    private void setTopAppBarOnClickListener() {
        profileTopAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout:
                        logoutUSer();
                        return true;
                }
                return false;
            }
        });
    }

    private void logoutUSer() {
        ParseUser.logOut();
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }

    private void bindAdapter(View view) {
        List<Vehicle> allVehicles = new ArrayList<>();
        adapter = new UserVehiclesAdapter(this, allVehicles, (MainActivity)getActivity());

        RecyclerView rvVehicles = view.findViewById(R.id.rvUserVehicles);
        rvVehicles.setAdapter(adapter);
        rvVehicles.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
    }

    private void bind(View view) {
        profileTopAppBar = view.findViewById(R.id.profileTopAppBar);
        tvProfileUserName = view.findViewById(R.id.tvProfileUserName);
        ivSetProfileImage = view.findViewById(R.id.ivSetProfileImage);
    }

    private void setValues() {
        _User currentUser = (_User) ParseUser.getCurrentUser();
        String UserName = currentUser.getFirstName() + " " + currentUser.getLastName();
        tvProfileUserName.setText(UserName);
    }

    private void queryUserVehicles() {
        _User currentUser = (_User) ParseUser.getCurrentUser();

        ParseQuery<Vehicle> query = ParseQuery.getQuery(Vehicle.class);
        query.whereEqualTo("owner", currentUser);
        query.findInBackground(new FindCallback<Vehicle>() {
            @Override
            public void done(List<Vehicle> vehicles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting the vehicles",e);
                    return;
                }
                adapter.addAll(vehicles);
            }
        });
    }


}