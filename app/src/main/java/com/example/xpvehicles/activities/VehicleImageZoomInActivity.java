package com.example.xpvehicles.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.xpvehicles.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.ortiz.touchview.TouchImageView;
import com.parse.ParseFile;

public class VehicleImageZoomInActivity extends AppCompatActivity {

    private static final String TAG = "VehicleImageZoomInActivity";
    private MaterialToolbar topAppBarVehicleImageZoomIn;
    private ParseFile image;
    private TouchImageView touchImageViewVehicleImageZoom;

    private void setTopAppBarOnClickListener() {
        topAppBarVehicleImageZoomIn.setNavigationOnClickListener(v -> {
            this.finish();
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_image_zoom_in);
        bind();
        setTopAppBarOnClickListener();
    }

    private void bind() {
        topAppBarVehicleImageZoomIn =  findViewById(R.id.topAppBarVehicleImageZoomIn);
        touchImageViewVehicleImageZoom =  findViewById(R.id.touchImageViewVehicleImageZoom);
        image = getIntent().getParcelableExtra("image");
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(touchImageViewVehicleImageZoom);
        }
    }
}