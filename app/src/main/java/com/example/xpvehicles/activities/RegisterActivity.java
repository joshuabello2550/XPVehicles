package com.example.xpvehicles.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.xpvehicles.R;
import com.example.xpvehicles.models._User;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtFirstName;
    private EditText edtLastName;
    private TextInputLayout registerFirstNameOTF;
    private TextInputLayout registerLastNameOTF;
    private TextInputLayout registerEmailOTF;
    private TextInputLayout registerPasswordOTF;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bind();
        setCreateAccountOnClickListener();
    }

    private void bind() {
        edtEmail = findViewById(R.id.edtRegisterEmail);
        edtPassword = findViewById(R.id.edtRegisterPassword);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        registerFirstNameOTF = findViewById(R.id.registerFirstNameOTF);
        registerLastNameOTF = findViewById(R.id.registerLastNameOTF);
        registerEmailOTF = findViewById(R.id.registerEmailOTF);
        registerPasswordOTF = findViewById(R.id.registerPasswordOTF);
    }

    private void setCreateAccountOnClickListener() {
        btnCreateAccount.setOnClickListener(v -> {
            resetErrors();
            String username = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            String firstName = edtFirstName.getText().toString();
            String lastName = edtLastName.getText().toString();

            Boolean shouldCreateNewUser = checkValues(username, password, firstName, lastName);
            if (shouldCreateNewUser) {
                saveUser(username, password, firstName, lastName);
            }
        });
    }

    private void resetErrors() {
        registerFirstNameOTF.setError(null);
        registerLastNameOTF.setError(null);
        registerEmailOTF.setError(null);
        registerPasswordOTF.setError(null);
    }

    private Boolean checkValues(String username, String password, String firstName, String lastName) {
        Boolean shouldCreateNewUser = true;
        if (firstName.isEmpty()) {
            registerFirstNameOTF.setError("Enter first name");
            shouldCreateNewUser = false;
        }
        if (lastName.isEmpty()){
            registerLastNameOTF.setError("Enter last name");
            shouldCreateNewUser = false;
        }
        if (username.isEmpty()){
            registerEmailOTF.setError("Enter email address");
            shouldCreateNewUser = false;
        } else {
            if (!MainActivity.isValidEmail(username)) {
                registerEmailOTF.setError("Invalid email address");
                shouldCreateNewUser = false;
            }
        }
        if (password.isEmpty()){
            registerPasswordOTF.setError("Enter password");
            shouldCreateNewUser = false;
        }
        return shouldCreateNewUser;
    }

    private void saveUser(String username, String password, String firstName, String lastName) {
            _User user = new _User();
            user.setUsername(username);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.signUpInBackground(e -> {
                if (e != null) {
                    Log.e(TAG, "error logging in", e);
                } else {
                    goMainActivity();
                }
            });
    }

    private void goMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}