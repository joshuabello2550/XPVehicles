package com.example.xpvehicles.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.LoginActivity;
import com.parse.ParseUser;


public class ProfileFragment extends Fragment {

    private Button btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_profile, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        btnLogout = view.findViewById(R.id.btnLogout);
        setLogoutOnClickListener();
    }

    private void setLogoutOnClickListener() {
        btnLogout.setOnClickListener(v -> {
            ParseUser.logOut();
            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);
        });
    }
}