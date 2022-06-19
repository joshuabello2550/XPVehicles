package com.example.xpvehicles.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xpvehicles.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtFirstName;
    private EditText edtLastName;
    private Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bind();
        createAccountOnClickListener();
    }

    private void bind() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
    }

    private void createAccountOnClickListener() {
        btnCreateAccount.setOnClickListener(v -> {

        });
    }

}