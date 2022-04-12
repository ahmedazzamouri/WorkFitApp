package com.example.workfitapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText name, email, password;
    Button register;
    ImageButton login;
    boolean isNameValid, isEmailValid, isPasswordValid;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.et_name);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        register = findViewById(R.id.btn_register);
        login = findViewById(R.id.button_login);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValidation();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setValidation(){
        if (name.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), R.string.name_error, Toast.LENGTH_SHORT).show();
            isNameValid = false;
        } else {
            isNameValid = true;
        }
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

        if (isNameValid && isEmailValid && isPasswordValid){
            Toast.makeText(getApplicationContext(),"Register successfully!", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(in);
        }
    }

}
