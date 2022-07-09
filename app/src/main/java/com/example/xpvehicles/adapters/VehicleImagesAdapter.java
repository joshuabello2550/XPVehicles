package com.example.xpvehicles.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.VehicleDetailsActivity;
import com.example.xpvehicles.activities.VehicleImageZoomInActivity;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;

public class VehicleImagesAdapter extends RecyclerView.Adapter<VehicleImagesAdapter.ViewHolder> {

    private Activity activity;
    private List<ParseFile> images;

    public VehicleImagesAdapter(Activity activity, List<ParseFile> images) {
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
        ParseFile image = images.get(position);
        holder.setValues(image);
        if (activity instanceof VehicleDetailsActivity) {
            holder.setImageOnClickListener(image);
        }

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

        public void setValues(ParseFile image) {
            if (image != null) {
                Glide.with(activity).load(image.getUrl()).into(imageView);
            }
        }

        public void setImageOnClickListener(ParseFile image) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, VehicleImageZoomInActivity.class);
                    intent.putExtra("image", image);
                    activity.startActivity(intent);
                }
            });
        }
    }
}
