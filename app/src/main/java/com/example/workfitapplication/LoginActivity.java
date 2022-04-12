package com.example.workfitapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login;
    ImageButton register;
    boolean isEmailValid, isPasswordValid;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.login_email);
        password=findViewById(R.id.login_pwd);
        login = findViewById(R.id.login_bttn);
        register = findViewById(R.id.register_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValidation();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);

            }
        });
    }

    public void setValidation(){
        if (email.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),R.string.email_error, Toast.LENGTH_SHORT).show();
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            Toast.makeText(getApplicationContext(),R.string.error_invalid_email, Toast.LENGTH_SHORT).show();
            isEmailValid = false;
        } else {
            isEmailValid = true;
        }

        if (password.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),R.string.password_error, Toast.LENGTH_SHORT).show();
            isPasswordValid = false;
        } else if (password.getText().length() < 6){
            Toast.makeText(getApplicationContext(),R.string.error_invalid_password, Toast.LENGTH_SHORT).show();
            isPasswordValid = false;
        } else {
            isPasswordValid = true;
        }

        if (isEmailValid && isPasswordValid){
            Toast.makeText(getApplicationContext(),"Login successfully!", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(in);
        }
    }
}
