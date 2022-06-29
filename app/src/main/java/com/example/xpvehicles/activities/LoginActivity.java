package com.example.xpvehicles.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            if (MainActivity.isValidEmail(username)) {
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
                goMainActivity();
            }
        });
    }


    private void goMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void setMakeNewAccountOnClickListener() {
        tvMakeNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}