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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

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
import com.parse.ParseFile;
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
        LayoutInflater inflater = LayoutInflater.from(fragment.getContext());
        View vehicleView =  inflater.inflate(R.layout.vehicle_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(vehicleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehicle vehicle = vehicles.get(position);
        holder.setValues(vehicle);
        holder.setSaveBtnOnClickListener(vehicle);
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
        private ImageView ivVehicle;
        private ImageButton ibSave;
        private _User currentUser;

        public ViewHolder(View itemView) {
            super(itemView);
            bind(itemView);
        }

        private void bind(View itemView) {
            tvVehicleName = itemView.findViewById(R.id.tvVehicleName);
            tvDistanceFromUser = itemView.findViewById(R.id.tvDistanceFromUser);
            ivVehicle = itemView.findViewById(R.id.ivVehicle);
            tvDailyPrice = itemView.findViewById(R.id.tvDailyPrice);
            ibSave = itemView.findViewById(R.id.ibSave);
        }

        public void setValues(Vehicle vehicle) {
            // Vehicle name
            tvVehicleName.setText(vehicle.getVehicleName());

            // daily price
            String dailyPrice = "$" + vehicle.getDailyPrice() + " /day";
            tvDailyPrice.setText(dailyPrice);

            // distance from user
            String distanceFromUser = vehicle.getDistanceFromUser();
            if (distanceFromUser != null) {
                tvDistanceFromUser.setText(distanceFromUser);
            } else {
                String vehiclePlaceId = vehicle.getPlaceId();
                getDistanceFromUser(vehicle, vehiclePlaceId, activity.getUserLocation());
            }

            // vehicle image
            ParseFile image = vehicle.getVehicleImage();
            if (image != null) {
                Glide.with(fragment.getContext()).load(image.getUrl()).into(ivVehicle);
            }
            setImageOnClickListener(vehicle);
        }

        private void getDistanceFromUser(Vehicle vehicle, String vehiclePlaceId, String userLocation) {
            final String GOOGLE_DISTANCE_MATRIX_API_BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?";
            final String GOOGLE_DISTANCE_MATRIX_API_PARAMETER_DESTINATIONS = "destinations";
            final String GOOGLE_DISTANCE_MATRIX_API_PARAMETER_ORIGINS = "origins";
            final String GOOGLE_DISTANCE_MATRIX_API_PARAMETER_KEY = "key";
            final String GOOGLE_DISTANCE_MATRIX_API_PARAMETER_UNITS = "units";

            Log.i(TAG, "Vehicles " + vehiclePlaceId + " " + userLocation);

            RequestParams params = new RequestParams();
            Resources res = fragment.getContext().getResources();
            params.put(GOOGLE_DISTANCE_MATRIX_API_PARAMETER_DESTINATIONS, "place_id:" + vehiclePlaceId);
            params.put(GOOGLE_DISTANCE_MATRIX_API_PARAMETER_ORIGINS, userLocation);
            params.put(GOOGLE_DISTANCE_MATRIX_API_PARAMETER_KEY, res.getString(R.string.API_KEY));
            params.put(GOOGLE_DISTANCE_MATRIX_API_PARAMETER_UNITS, "imperial");

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(GOOGLE_DISTANCE_MATRIX_API_BASE_URL, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "Success " + json.toString());
                    JSONObject jsonObject = json.jsonObject;
                    try {
                        JSONArray elements = jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");
                        String distance = elements.getJSONObject(0).getJSONObject("distance").getString("text");
                        vehicle.setDistanceFromUser(distance);
                        tvDistanceFromUser.setText(distance);
                    } catch (JSONException e) {
                        Log.e(TAG, "Error retrieve distance from json object", e);
                    }
                }
                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "Error getting the distance from the user" + response, throwable);
                }
            });
        }

        private void setImageOnClickListener(Vehicle vehicle) {
            ivVehicle.setOnClickListener(v -> {
                Intent intent = new Intent(fragment.getContext(), VehicleDetailsActivity.class);
                intent.putExtra("vehicle", vehicle);
                fragment.startActivity(intent);
            });
        }

        private void setSaveBtnOnClickListener(Vehicle vehicle) {
            currentUser = (_User) ParseUser.getCurrentUser();
            List listSavedVehicles = currentUser.getSavedVehicles();
            if (listSavedVehicles.contains(vehicle.getObjectId())) {
                setSaveButtonClickedStyle(ibSave);
            } else {
                ibSave.setImageResource(R.drawable.ic_favorite_border_24);
            }

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
