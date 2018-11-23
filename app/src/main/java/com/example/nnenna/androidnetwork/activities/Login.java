package com.example.nnenna.androidnetwork.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nnenna.androidnetwork.R;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    private EditText etEmail,etPassword;
    private Button btnLogin,btnToRegister;

    private String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        etEmail= findViewById(R.id.etEmail);
        etPassword= findViewById(R.id.etPassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnToRegister=findViewById(R.id.btnReg);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginValidation();
            }
        });
        btnToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void loginValidation() {
        email=etEmail.getText().toString().trim();
        password= etPassword.getText().toString().trim();

        if (!isValidEmaillId(email)) {
            etEmail.setError("pls enter valid email");
            return;

        }
        if (password.length() < 8) {
            etPassword.setError("Password cannot be less than 8");
            return;

        }
        else {
            Toast.makeText(this, "validation successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Login.this,MainActivity.class));
        }
    }

    private void register() {
        startActivity(new Intent(Login.this,Registration.class));
    }

    boolean isValidEmaillId(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}
