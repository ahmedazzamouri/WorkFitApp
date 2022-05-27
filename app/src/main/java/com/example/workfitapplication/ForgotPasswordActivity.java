package com.example.workfitapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button buttonPwdReset;
    private EditText etPwdReset;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etPwdReset = findViewById(R.id.et_password_reset);

        buttonPwdReset = findViewById(R.id.button_reset_pwd);

        buttonPwdReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = etPwdReset.getText().toString();

                if(TextUtils.isEmpty(mail)){
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter a registered email", Toast.LENGTH_SHORT).show();
                    etPwdReset.setError("Email is required");
                    etPwdReset.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
                    Toast.makeText(ForgotPasswordActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                } else {
                    resetPassword(mail);
                }
            }
        });

    }

    private void resetPassword(String mail) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this, "Please check your inbox for password reset link", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
