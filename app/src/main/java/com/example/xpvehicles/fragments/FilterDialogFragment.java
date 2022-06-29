package com.example.xpvehicles.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xpvehicles.R;
import com.google.android.material.appbar.MaterialToolbar;

public class FilterDialogFragment extends DialogFragment {

    public static final String TAG = "FilterDialogFragment";
    private MaterialToolbar filterTopAppBar;

    public static FilterDialogFragment displayFilterDialogFragment(FragmentManager fragmentManager) {
        FilterDialogFragment exampleDialog = new FilterDialogFragment();
        exampleDialog.show(fragmentManager, TAG);
        return exampleDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        bind(view);
        setTopAppBarOnClickListener();
    }

    private void bind(View view) {
        filterTopAppBar = view.findViewById(R.id.filterTopAppBar);
    }

    private void setTopAppBarOnClickListener() {
        filterTopAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}