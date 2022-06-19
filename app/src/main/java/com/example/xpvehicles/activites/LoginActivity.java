package com.example.xpvehicles.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xpvehicles.R;

public class LoginActivity extends AppCompatActivity {

    private EditText edtLoginEmail;
    private EditText edtLoginPassword;
    private Button btnLogin;
    private TextView tvMakeNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bind();
        newAccountOnClickListener();
//        loginOnClickListener();
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
        });
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