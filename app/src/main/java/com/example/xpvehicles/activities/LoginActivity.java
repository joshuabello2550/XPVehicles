package com.example.xpvehicles.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xpvehicles.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login_Activity";
    private EditText edtLoginEmail;
    private EditText edtLoginPassword;
    private Button btnLogin;
    private TextView tvMakeNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // navigate to Main_Activity if a user is already logged in.
        if (ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        bind();
        newAccountOnClickListener();
        loginOnClickListener();
    }

    private void bind() {
        edtLoginEmail =  findViewById(R.id.edtLoginEmail);
        edtLoginPassword =  findViewById(R.id.edtLoginPassword);
        btnLogin =  findViewById(R.id.btnLogin);
        tvMakeNewAccount = findViewById(R.id.tvMakeNewAccount);
    }

    private void loginOnClickListener() {
        btnLogin.setOnClickListener(v -> {
            // TODO: finish login button
            String username = edtLoginEmail.getText().toString();
            String password = edtLoginPassword.getText().toString();
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issue with login", e);
                        return;
                    }
                    goMainActivity();
                }
            });
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void newAccountOnClickListener() {
        tvMakeNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}