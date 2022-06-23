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
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Headers;

public class AddVehicleActivity extends AppCompatActivity {

    private static final String TAG = "Add_Vehicle_Activity";
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
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 10;
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        bind();
        setTopAppBarOnClickListener();
        setAddVehicleOnClickListener();
        setTakePictureOnClickListener();
    }

    private void bind() {
        topAppBar = findViewById(R.id.topAppBar);
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
    }

    private void setTopAppBarOnClickListener() {
        topAppBar.setNavigationOnClickListener(v -> {
            this.finish();
        });
    }

    private void setTakePictureOnClickListener() {
        String photoFileName = "photo.jpg";
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create Intent to take a picture and return control to the calling application
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Create a File reference for future access
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
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ImageView ivPreview = (ImageView) findViewById(R.id.ivAddVehicleImage);
                ivAddVehicleImage.setVisibility(View.VISIBLE);
                ivPreview.setImageBitmap(takenImage);
            } else { // Result was a failure
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
            getPlaceId(streetAddress, city, state);
        });
    }

    private void getPlaceId(String streetAddress, String city, String state) {
        String base_url = "https://maps.googleapis.com/maps/api/geocode/json?";
        String address = streetAddress + " " + city + " " + state;

        RequestParams params = new RequestParams();
        Resources res = this.getResources();
        params.put("key", res.getString(R.string.API_KEY));
        params.put("address", address);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(base_url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    String placeId = jsonObject.getJSONArray("results").getJSONObject(0).getString("place_id");
                    saveVehicle(placeId);
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

    private void saveVehicle(String placeId) {
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
        vehicle.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving the vehicle to parse", e);
                    return;
                }
                Log.i(TAG, "Post was successful");
                clearComponents();
            }
        });
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