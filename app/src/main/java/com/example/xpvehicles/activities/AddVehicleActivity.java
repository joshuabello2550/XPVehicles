package com.example.xpvehicles.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.xpvehicles.R;
import com.example.xpvehicles.models.Vehicle;
import com.google.android.material.appbar.MaterialToolbar;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Headers;

public class AddVehicleActivity extends AppCompatActivity {

    private static final String TAG = "AddVehicleActivity";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 10;
    private MaterialToolbar topAppBar;
    private EditText edtVehicleName;
    private EditText edtDescription;
    private EditText edtStreetAddress;
    private EditText edtCity;
    private EditText edtState;
    private EditText edtZipCode;
    private EditText edtDailyPrice;
    private ImageView ivAddVehicleImage;
    private Button btnAddVehicle;
    private Button btnTakePicture;
    private File photoFile;
    private FrameLayout takePictureFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        bind();
        setTopAppBarOnClickListener();
        setAddVehicleOnClickListener();
        setTakePictureOnClickListener();
    }

    private void setTopAppBarOnClickListener() {
        topAppBar.setNavigationOnClickListener(v -> {
            this.finish();
        });
    }

    private void bind() {
        topAppBar = findViewById(R.id.filterTopAppBar);
        edtVehicleName = findViewById(R.id.edtVehicleName);
        edtDescription = findViewById(R.id.edtDescription);
        edtStreetAddress = findViewById(R.id.edtStreetAddress);
        edtCity = findViewById(R.id.edtCity);
        edtState = findViewById(R.id.edtState);
        edtZipCode = findViewById(R.id.edtZipCode);
        edtDailyPrice = findViewById(R.id.edtDailyPrice);
        ivAddVehicleImage = findViewById(R.id.ivAddVehicleImage);
        btnAddVehicle = findViewById(R.id.btnAddVehicle);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        takePictureFrameLayout = findViewById(R.id.takePictureFrameLayout);
    }

    private void setTakePictureOnClickListener() {
        String photoFileName = "photo.jpg";
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoFile = getPhotoFileUri(photoFileName);

                // wrap File object into a content provider
                Uri fileProvider = FileProvider.getUriForFile(AddVehicleActivity.this, "com.codepath.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                // So as long as the result is not null, it's safe to use the intent.
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Start the image capture intent to take photo
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }
            }
        });
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ImageView ivPreview = (ImageView) findViewById(R.id.ivAddVehicleImage);
                takePictureFrameLayout.setVisibility(View.GONE);
                ivAddVehicleImage.setVisibility(View.VISIBLE);
                ivPreview.setImageBitmap(takenImage);
            } else {
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setAddVehicleOnClickListener() {
        btnAddVehicle.setOnClickListener(v -> {
            String streetAddress = edtStreetAddress.getText().toString();
            String city = edtCity.getText().toString();
            String state = edtState.getText().toString();
            String zipCode = edtZipCode.getText().toString();
            getPlaceIdAndGeoPoint(streetAddress, city, state);
        });
    }

    private void getPlaceIdAndGeoPoint(String streetAddress, String city, String state) {
        final String GOOGLE_GEOCODING_API_BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json?";
        final String GOOGLE_GEOCODING_API_PARAMETER_KEY = "key";
        final String GOOGLE_GEOCODING_API_PARAMETER_ADDRESS = "address";

        String address = streetAddress + " " + city + " " + state;
        RequestParams params = new RequestParams();
        Resources res = this.getResources();
        params.put(GOOGLE_GEOCODING_API_PARAMETER_KEY, res.getString(R.string.API_KEY));
        params.put(GOOGLE_GEOCODING_API_PARAMETER_ADDRESS, address);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(GOOGLE_GEOCODING_API_BASE_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    String placeId = jsonObject.getJSONArray("results").getJSONObject(0).getString("place_id");
                    JSONObject location = jsonObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                    Double vehicleLatitude =  location.getDouble("lat");
                    Double vehicleLongitude =  location.getDouble("lng");
                    ParseGeoPoint vehicleLocationGeoPoint =  new ParseGeoPoint(vehicleLatitude, vehicleLongitude);
                    saveVehicle(placeId, vehicleLocationGeoPoint);
                } catch (JSONException e) {
                    Log.e(TAG, "hit json exception when getting the latitude and longitude");
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Error sending the JSON request", throwable);
            }
        });
    }

    private void saveVehicle(String placeId, ParseGeoPoint vehicleLocationGeoPoint) {
        String vehicleName = edtVehicleName.getText().toString();
        String description = edtDescription.getText().toString();
        Double dailyPrice = Double.valueOf(edtDailyPrice.getText().toString());
        ParseUser owner = ParseUser.getCurrentUser();

        Vehicle vehicle = new Vehicle();
        vehicle.setOwner(owner);
        vehicle.setVehicleName(vehicleName);
        vehicle.setDescription(description);
        vehicle.setDailyPrice(dailyPrice);
        vehicle.setPlaceId(placeId);
        vehicle.setVehicleImage(new ParseFile(photoFile));
        vehicle.setGeoLocation(vehicleLocationGeoPoint);
        vehicle.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving the vehicle to parse", e);
                    Toast.makeText(AddVehicleActivity.this, "Unable to save vehicle to parse", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "Post was successful");
                clearComponents();
                goMainActivity();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void clearComponents() {
        edtVehicleName.setText("");
        edtDescription.setText("");
        edtStreetAddress.setText("");
        edtCity.setText("");
        edtState.setText("");
        edtZipCode.setText("");
        edtDailyPrice.setText("");
        ivAddVehicleImage.setVisibility(View.GONE);
    }
}