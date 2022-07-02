package com.example.xpvehicles.adapters;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.activities.VehicleDetailsActivity;
import com.example.xpvehicles.fragments.ExploreFragment;
import com.example.xpvehicles.models.RentVehicle;
import com.example.xpvehicles.models.Vehicle;
import com.example.xpvehicles.models._User;
import com.google.android.material.card.MaterialCardView;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Headers;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ViewHolder> {

    public static final String TAG = "ExploreAdapter";
    private List<Vehicle> vehicles;
    private Fragment fragment;
    private MainActivity activity;

    public ExploreAdapter(Fragment fragment, List<Vehicle> vehicles, MainActivity activity){
        this.vehicles = vehicles;
        this.fragment = fragment;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vehicleView =  LayoutInflater.from(fragment.getContext()).inflate(R.layout.vehicle_card, parent, false);
        return new ViewHolder(vehicleView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehicle vehicle = vehicles.get(position);
        holder.setValues(vehicle);
        holder.setSaveBtnInitialColor(vehicle);
        holder.setSaveBtnOnClickListener(vehicle);
        holder.setMaterialCardVehicleOnClickListener(vehicle);
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    public void addAll(List<Vehicle> allVehicles) {
        vehicles.addAll(allVehicles);
        notifyDataSetChanged();
    }

    public void clear() {
        vehicles.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvVehicleName;
        private TextView tvDistanceFromUser;
        private TextView tvDailyPrice;
        private ImageButton ibSave;
        private MaterialCardView materialCardVehicle;
        private ViewPager2 viewPager;

        public ViewHolder(View itemView) {
            super(itemView);
            bind(itemView);
        }

        private void bind(View itemView) {
            tvVehicleName = itemView.findViewById(R.id.tvVehicleName);
            tvDistanceFromUser = itemView.findViewById(R.id.tvDistanceFromUser);
            tvDailyPrice = itemView.findViewById(R.id.tvDailyPrice);
            ibSave = itemView.findViewById(R.id.ibSave);
            materialCardVehicle = itemView.findViewById(R.id.materialCardVehicle);
            viewPager = itemView.findViewById(R.id.viewPagerExploreVehicleImages);
        }

        private void setValues(Vehicle vehicle) {
            // Vehicle name
            tvVehicleName.setText(vehicle.getVehicleName());

            // daily price
            String dailyPrice = "$" + vehicle.getDailyPrice() + " /day";
            tvDailyPrice.setText(dailyPrice);

            // distance from user
            if (MainActivity.getUserLocationGeoPoint() != null) {
                int distanceFromUser = MainActivity.getDistanceFromUser(vehicle);
                tvDistanceFromUser.setText(distanceFromUser + " mi.");
            }

            // vehicle image
            bindVehicleImagesAdapter(vehicle);
        }

        private void bindVehicleImagesAdapter(Vehicle vehicle) {
            List<ParseFile> images = vehicle.getVehicleImages();
            VehicleImagesAdapter vehicleImagesAdapter =  new VehicleImagesAdapter(activity, images);
            viewPager.setAdapter(vehicleImagesAdapter);
//            setVehicleSwipeListener(viewPager, images.size());
        }

        private void setMaterialCardVehicleOnClickListener(Vehicle vehicle) {
            materialCardVehicle.setOnClickListener(v -> {
                Intent intent = new Intent(fragment.getContext(), VehicleDetailsActivity.class);
                intent.putExtra("vehicle", vehicle);
                activity.startActivity(intent);
            });
        }

        private void setSaveBtnInitialColor(Vehicle vehicle) {
            _User currentUser = (_User) ParseUser.getCurrentUser();
            List listSavedVehicles = currentUser.getSavedVehicles();
            if (listSavedVehicles.contains(vehicle.getObjectId())) {
                setSaveButtonClickedStyle(ibSave);
            } else {
                ibSave.setImageResource(R.drawable.ic_favorite_border_24);
            }
        }

        private void setSaveBtnOnClickListener(Vehicle vehicle) {
            _User currentUser = (_User) ParseUser.getCurrentUser();
            List listSavedVehicles = currentUser.getSavedVehicles();
            ibSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listSavedVehicles.contains(vehicle.getObjectId())) {
                        listSavedVehicles.remove(vehicle.getObjectId());
                        ibSave.setImageResource(R.drawable.ic_favorite_border_24);
                    } else {
                        listSavedVehicles.add(vehicle.getObjectId());
                        setSaveButtonClickedStyle(ibSave);
                    }
                    currentUser.setSavedVehicles(listSavedVehicles);
                    currentUser.saveInBackground();
                }
            });
        }

        private void setSaveButtonClickedStyle(ImageButton ibSave) {
            Drawable img = fragment.getActivity().getDrawable(R.drawable.ic_favorite_24);
            Resources res = fragment.getContext().getResources();
            img.setTint(res.getColor(R.color.md_theme_light_primary, activity.getTheme()));
            ibSave.setImageDrawable(img);
        }
    }
}
