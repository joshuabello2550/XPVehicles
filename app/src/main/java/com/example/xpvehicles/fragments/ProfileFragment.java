package com.example.xpvehicles.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.AddVehicleActivity;
import com.example.xpvehicles.activities.LoginActivity;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.adapters.ExploreAdapter;
import com.example.xpvehicles.adapters.UserVehiclesAdapter;
import com.example.xpvehicles.models.Vehicle;
import com.example.xpvehicles.models._User;
import com.google.android.material.appbar.MaterialToolbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ProfileFragment extends Fragment {

    private static final String TAG = "Profile_Fragment";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 10;
    private File photoFile;
    private UserVehiclesAdapter adapter;
    private MaterialToolbar profileTopAppBar;
    private MainActivity activity;
    private TextView tvProfileUserName;
    private TextView tvUserNoVehiclesListed;
    private ImageView ivSetProfileImage;
    private ImageView ivProfileImage;

    public ProfileFragment(MainActivity mainActivity) {
        activity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, parent, false);
    }

    private void setTopAppBarOnClickListener() {
        profileTopAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout:
                        logoutUSer();
                        return true;
                }
                return false;
            }
        });
    }

    private void logoutUSer() {
        ParseUser.logOut();
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        bind(view);
        queryUserVehicles();
        bindAdapter(view);
        setValues();
        setTopAppBarOnClickListener();
        setCaptureProfileImageOnClickListener();
    }

    private void bindAdapter(View view) {
        List<Vehicle> allVehicles = new ArrayList<>();
        adapter = new UserVehiclesAdapter(this, allVehicles, (MainActivity)getActivity());

        RecyclerView rvVehicles = view.findViewById(R.id.rvUserVehicles);
        rvVehicles.setAdapter(adapter);
        rvVehicles.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
    }

    private void bind(View view) {
        profileTopAppBar = view.findViewById(R.id.profileTopAppBar);
        tvProfileUserName = view.findViewById(R.id.tvProfileUserName);
        ivSetProfileImage = view.findViewById(R.id.ivSetProfileImage);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        tvUserNoVehiclesListed = view.findViewById(R.id.tvUserNoVehiclesListed);
    }

    private void setValues() {
        _User currentUser = (_User) ParseUser.getCurrentUser();
        String UserName = currentUser.getFirstName() + " " + currentUser.getLastName();
        tvProfileUserName.setText(UserName);

        ParseFile profileImage = currentUser.getProfileImage();
        if (profileImage != null) {
            Glide.with(activity).load(profileImage.getUrl()).into(ivProfileImage);
        }
    }

    private void queryUserVehicles() {
        _User currentUser = (_User) ParseUser.getCurrentUser();

        ParseQuery<Vehicle> query = ParseQuery.getQuery(Vehicle.class);
        query.whereEqualTo("owner", currentUser);
        query.findInBackground(new FindCallback<Vehicle>() {
            @Override
            public void done(List<Vehicle> vehicles, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting the user's vehicles",e);
                    return;
                }
                if (vehicles.size() > 0) {
                    tvUserNoVehiclesListed.setVisibility(View.GONE);
                    adapter.addAll(vehicles);
                } else {
                    tvUserNoVehiclesListed.setVisibility(View.VISIBLE);
                }
                adapter.addAll(vehicles);
            }
        });
    }

    private void setCaptureProfileImageOnClickListener() {
        String photoFileName = "profilePhoto.jpg";
        ivSetProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create Intent to take a picture and return control to the calling application
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Create a File reference for future access
                photoFile = getPhotoFileUri(photoFileName);

                // wrap File object into a content provider
                Uri fileProvider = FileProvider.getUriForFile(activity, "com.codepath.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                // So as long as the result is not null, it's safe to use the intent.
                if (intent.resolveActivity(activity.getPackageManager()) != null) {
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
        File mediaStorageDir = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

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
                ivProfileImage.setImageBitmap(takenImage);
                // save image in Parse Database
                _User currentUser = (_User) ParseUser.getCurrentUser();
                ParseFile profileImage = conversionBitmapParseFile(takenImage);
                currentUser.setProfileImage(profileImage);
                currentUser.saveInBackground();

            } else { // Result was a failure
                Toast.makeText(activity, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ParseFile conversionBitmapParseFile(Bitmap imageBitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        ParseFile parseFile = new ParseFile("image_file.png",imageByte);
        return parseFile;
    }



}