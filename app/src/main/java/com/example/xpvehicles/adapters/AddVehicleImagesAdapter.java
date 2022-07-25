package com.example.xpvehicles.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.RentingRequestsActivity;
import com.example.xpvehicles.activities.VehicleDetailsActivity;
import com.example.xpvehicles.activities.VehicleImageZoomInActivity;
import com.parse.ParseFile;

import java.io.File;
import java.util.List;

public class AddVehicleImagesAdapter extends RecyclerView.Adapter<AddVehicleImagesAdapter.ViewHolder> {

    private Activity activity;
    private List<File> images;

    public AddVehicleImagesAdapter(Activity activity, List<File> images) {
        this.activity = activity;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.vehicle_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File image = images.get(position);
        holder.setValues(image);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bind();
        }

        private void bind() {
            imageView = itemView.findViewById(R.id.imageViewMain);
        }

        public void setValues(File image) {
            if (image != null) {
                Glide.with(activity).load(image).into(imageView);
            }
        }
    }
}
