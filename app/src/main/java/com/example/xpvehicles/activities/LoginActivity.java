package com.example.xpvehicles.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xpvehicles.miscellaneous.EmailChecker;
import com.example.xpvehicles.R;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText edtLoginEmail;
    private EditText edtLoginPassword;
    private Button btnLogin;
    private TextView tvMakeNewAccount;
    private TextInputLayout loginEmailOTF;
    private TextInputLayout loginPasswordOTF;

    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(this, "granted", Toast.LENGTH_SHORT);
                    // FCM SDK (and your app) can post notifications.
                } else {
                    Toast.makeText(this, "not granted", Toast.LENGTH_SHORT);
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
            // FCM SDK (and your app) can post notifications.
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            // TODO: display an educational UI explaining to the user the features that will be enabled
            //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
            //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
            //       If the user selects "No thanks," allow the user to continue without notifications.
        } else {
            // Directly ask for the permission
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        askNotificationPermission();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // navigate to Main_Activity if a user is already logged in.
        if (ParseUser.getCurrentUser() != null){
            goMainActivity();
        }
        bind();
        setMakeNewAccountOnClickListener();
        setLoginOnClickListener();
    }

    private void bind() {
        edtLoginEmail =  findViewById(R.id.edtLoginEmail);
        edtLoginPassword =  findViewById(R.id.edtLoginPassword);
        btnLogin =  findViewById(R.id.btnLogin);
        tvMakeNewAccount = findViewById(R.id.tvMakeNewAccount);
        loginEmailOTF = findViewById(R.id.loginEmailOTF);
        loginPasswordOTF = findViewById(R.id.loginPasswordOTF);
    }

    private void setLoginOnClickListener() {
        btnLogin.setOnClickListener(v -> {
            resetErrors();
            String username = edtLoginEmail.getText().toString();
            String password = edtLoginPassword.getText().toString();

            Boolean loginUser = checkValues(username, password);
            if (loginUser) {
                loginUser(username, password);
            }
        });
    }

    private void resetErrors() {
        loginEmailOTF.setError(null);
        loginPasswordOTF.setError(null);
    }

    private Boolean checkValues(String username, String password) {
        Boolean shouldCreateNewUser = true;
        if (username.isEmpty()){
            loginEmailOTF.setError("Enter email address");
            shouldCreateNewUser = false;
        } else {
            if (!EmailChecker.isValidEmail(username)) {
                loginEmailOTF.setError("Invalid email address");
                shouldCreateNewUser = false;
            }
        }
        if (password.isEmpty()){
            loginPasswordOTF.setError("Enter password");
            shouldCreateNewUser = false;
        }
        return shouldCreateNewUser;
    }

    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with login", e);
                    loginEmailOTF.setError("Incorrect email or password");
                    loginPasswordOTF.setError("Incorrect email or password");
                    return;
                }
//                hideSoftKeyboard(getCurrentFocus());
                goMainActivity();
            }
        });
    }

//    public void hideSoftKeyboard(View view){
//        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }

    private void goMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void setMakeNewAccountOnClickListener() {
        tvMakeNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}