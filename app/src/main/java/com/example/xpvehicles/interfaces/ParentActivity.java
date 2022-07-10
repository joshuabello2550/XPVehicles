package com.example.xpvehicles.interfaces;

import android.app.Activity;

import com.google.android.material.appbar.MaterialToolbar;

public interface ParentActivity {

    default void setTopAppBarOnClickListener(MaterialToolbar topAppBar, Activity activity) {
        topAppBar.setNavigationOnClickListener(v -> {
            activity.finish();
        });
    }
}
