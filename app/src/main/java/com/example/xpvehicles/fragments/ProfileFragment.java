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
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xpvehicles.R;
import com.example.xpvehicles.activities.LoginActivity;
import com.example.xpvehicles.activities.MainActivity;
import com.example.xpvehicles.adapters.UserOwnedVehiclesAdapter;
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
import java.util.List;


public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 10;
    private File photoFile;
    private UserOwnedVehiclesAdapter adapter;
    private MaterialToolbar profileTopAppBar;
    private TextView tvProfileUserName;
    private TextView tvUserNoVehiclesListed;
    private ImageView ivSetProfileImage;
    private ImageView ivProfileImage;

    public ProfileFragment() {
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
        adapter = new UserOwnedVehiclesAdapter(this, allVehicles, (MainActivity)getActivity());

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
            Glide.with(getActivity()).load(profileImage.getUrl()).into(ivProfileImage);
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
                adapter.setVehicles(vehicles, tvUserNoVehiclesListed);
            }
        });
    }

    private void setCaptureProfileImageOnClickListener() {
        String photoFileName = "profilePhoto.jpg";
        ivSetProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoFile = getPhotoFileUri(photoFileName);

                // wrap File object into a content provider
                Uri fileProvider = FileProvider.getUriForFile(getActivity(), "com.codepath.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                // So as long as the result is not null, it's safe to use the intent.
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Start the image capture intent to take photo
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }
            }
        });
    }

    // Returns the File for a photo stored on disk given the fileName
    private File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

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
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivProfileImage.setImageBitmap(takenImage);
                // save image in Parse Database
                _User currentUser = (_User) ParseUser.getCurrentUser();
                currentUser.setProfileImage(new ParseFile(photoFile));
                currentUser.saveInBackground();

            } else {
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}