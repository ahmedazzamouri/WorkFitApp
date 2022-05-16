package com.example.workfitapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView toolbarUserImg;
    private FirebaseAuth mAuth;
    private Button login;
    private TextInputLayout inputEmail, inputPwd;
    private TextView registerAccount;

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        toolbarUserImg = findViewById(R.id.toolbar_user_img);
        toolbarUserImg.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.login_email);
        inputPwd = findViewById(R.id.login_password);

        registerAccount= findViewById(R.id.register_account);
        login = findViewById(R.id.login_button);
        login.setOnClickListener(view -> LoggingUser());

        registerAccount.setOnClickListener(view -> CreateAccountUser());
    }

    private void CreateAccountUser() {
        Intent createAccountIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(createAccountIntent);
        finish();
    }


    // check if user can log in or not
    private void LoggingUser() {
        String email = inputEmail.getEditText().getText().toString().trim();
        String password = inputPwd.getEditText().getText().toString().trim();

        if (email.isEmpty()){
            inputEmail.setError("Please enter an email address!");

        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty()){
            inputPwd.setError("Please enter a password!");
        } else {
            inputPwd.setError(null);
        }

        if (!email.isEmpty() && !password.isEmpty()){
            loading = new ProgressDialog(this);
            String ProgressDialogMessage="Logging...";
            SpannableString spannableMsg = new SpannableString(ProgressDialogMessage);
            spannableMsg.setSpan(new RelativeSizeSpan(1.3f), 0, spannableMsg.length(), 0);
            loading.setMessage(spannableMsg);
            loading.show();
            loading.setCanceledOnTouchOutside(false);
            loading.setCancelable(false);


            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        sendUserToHome();
                    } else {
                        loading.cancel();
                        inputPwd.getEditText().setText("");

                        String msg = task.getException().getMessage();
                        final Dialog errorDialog = new Dialog(LoginActivity.this);
                        errorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        errorDialog.setContentView(R.layout.error_layout);
                        errorDialog.setTitle("Error Window");
                        errorDialog.show();
                        errorDialog.setCanceledOnTouchOutside(false);

                        TextView error = errorDialog.findViewById(R.id.error_dialog_error_message);
                        error.setText(msg);

                        Button cancelBtn = errorDialog.findViewById(R.id.error_dialog_cancel_button);
                        cancelBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                errorDialog.cancel();
                            }
                        });
                    }
                }
            });
        }

    }

    private void sendUserToHome() {
        Intent main = new Intent(LoginActivity.this, MainActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        main.putExtra("intentFrom", "LoginActivity");
        startActivity(main);
        finish();
    }


    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}