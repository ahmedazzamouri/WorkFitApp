package com.example.workfitapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userDatabaseReference;

    private TextInputLayout registerFullname, registerUsername, registerEmail, registerPwd, registerConfirmPwd, registerWeight, registerHeight,registerPhoneNumber;
    private Button registerBttn;
    private String name, username, phoneNumber, userMail, userPwd, userConfirmPwd, weight, height;

    private ProgressDialog loading;

    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        loading= new ProgressDialog(this);

        registerFullname = findViewById(R.id.register_name);
        registerUsername = findViewById(R.id.register_username);
        registerEmail = findViewById(R.id.register_email);
        registerPhoneNumber = findViewById(R.id.register_phone);
        registerPwd = findViewById(R.id.register_password);
        registerConfirmPwd = findViewById(R.id.register_confirm_password);
        registerWeight = findViewById(R.id.register_weight);
        registerHeight = findViewById(R.id.register_height);
        registerBttn = findViewById(R.id.register_button);

        registerBttn.setOnClickListener(view -> RegisterNewUser());
    }

    private void RegisterNewUser() {
        name = registerFullname.getEditText().getText().toString();
        username = registerUsername.getEditText().getText().toString();
        userMail = registerEmail.getEditText().getText().toString();
        phoneNumber = registerPhoneNumber.getEditText().getText().toString();
        userPwd = registerPwd.getEditText().getText().toString();
        userConfirmPwd = registerConfirmPwd.getEditText().getText().toString();
        weight = registerWeight.getEditText().getText().toString();
        height = registerHeight.getEditText().getText().toString();

        if (TextUtils.isEmpty(name)){
            registerFullname.setError("Please enter your name");
        } else {
            registerFullname.setError(null);
        }

        if (TextUtils.isEmpty(username)){
            registerUsername.setError("Please enter a username");
        } else {
            registerUsername.setError(null);
        }

        if (TextUtils.isEmpty(userMail)){
            registerEmail.setError("Please enter an email");
        } else {
            registerEmail.setError(null);
        }

        if (TextUtils.isEmpty(phoneNumber)){
            registerPhoneNumber.setError("Please enter a phone number");
        } else {
            registerPhoneNumber.setError(null);
        }

        if (TextUtils.isEmpty(userPwd)){
            registerPwd.setError("Please enter a password");
        } else {
            registerPwd.setError(null);
        }


        if (TextUtils.isEmpty(userConfirmPwd)){
            registerConfirmPwd.setError("Please confirm the password");
        } else {
            registerConfirmPwd.setError(null);
        }

        if (TextUtils.isEmpty(weight)){
            registerWeight.setError("Please enter your weight");
        } else {
            registerWeight.setError(null);
        }

        if (TextUtils.isEmpty(height)){
            registerHeight.setError("Please enter your height");
        } else {
            registerHeight.setError(null);
        }

        if (!TextUtils.isEmpty(userPwd) && !TextUtils.isEmpty(userConfirmPwd)){

            if (!userPwd.equals(userConfirmPwd)){
                registerConfirmPwd.setError("The password doesn't match");
            } else {
                registerConfirmPwd.setError(null);
            }
        }

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(userMail) && !TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(weight) && !TextUtils.isEmpty(height) &&  !TextUtils.isEmpty(userPwd) && !TextUtils.isEmpty(userConfirmPwd) && userPwd.equals(userConfirmPwd)){
            loading = new ProgressDialog(this);
            String ProgressDialogMessage="Registering...";
            SpannableString spannableMsg = new SpannableString(ProgressDialogMessage);
            spannableMsg.setSpan(new RelativeSizeSpan(1.3f), 0, spannableMsg.length(), 0);
            loading.setMessage(spannableMsg);
            loading.show();
            loading.setCanceledOnTouchOutside(false);
            loading.setCancelable(false);


            firebaseAuth.createUserWithEmailAndPassword(userMail, userPwd).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    assert user != null;
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RegisterActivity.this,"Email verification link sent", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AddDetailsToDatabase();
                } else {
                    registerPwd.getEditText().setText("");
                    registerConfirmPwd.getEditText().setText("");

                    loading.dismiss();

                    String msg = task.getException().getMessage();

                    final Dialog errorDialog = new Dialog(RegisterActivity.this);
                    errorDialog.setContentView(R.layout.error_layout);
                    errorDialog.setTitle("Error Window");
                    errorDialog.show();
                    errorDialog.setCanceledOnTouchOutside(false);

                    TextView error = errorDialog.findViewById(R.id.error_dialog_error_message);
                    error.setText(msg);

                    Button cancelBtn = errorDialog.findViewById(R.id.error_dialog_cancel_button);
                    cancelBtn.setOnClickListener(view -> errorDialog.cancel());
                }
            });

        }
    }

    private void AddDetailsToDatabase() {

        currentUserID = firebaseAuth.getCurrentUser().getUid();
        userDatabaseReference = FirebaseDatabase.getInstance("https://workfitapplication-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Users").child(currentUserID);

        String joinedDate = new SimpleDateFormat("dd MM yyyy", Locale.getDefault()).format(new Date());

        HashMap<String, Object> usermap = new HashMap<>();

        usermap.put("user_fullname", name);
        usermap.put("usersearchkeyword", username.toLowerCase());
        usermap.put("username", username);
        usermap.put("useremail", userMail);
        usermap.put("user_weight", weight);
        usermap.put("user_height", height);
        usermap.put("user_phone", phoneNumber);
        usermap.put("userjoineddate", joinedDate);

        userDatabaseReference.updateChildren(usermap).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                loading.dismiss();
                SendUserToHomePage();
            } else {
                registerPwd.getEditText().setText("");
                registerConfirmPwd.getEditText().setText("");

                loading.dismiss();

                String msg = task.getException().getMessage();

                final Dialog errorDialog = new Dialog(RegisterActivity.this);
                errorDialog.setContentView(R.layout.error_layout);
                errorDialog.setTitle("Error Window");
                errorDialog.show();
                errorDialog.setCanceledOnTouchOutside(false);

                TextView error = errorDialog.findViewById(R.id.error_dialog_error_message);
                error.setText(msg);

                Button cancelBtn = errorDialog.findViewById(R.id.error_dialog_cancel_button);
                cancelBtn.setOnClickListener(view -> errorDialog.cancel());

            }
        });
    }

    private void SendUserToHomePage() {
        Intent setupIntent = new Intent(RegisterActivity.this, MainActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        setupIntent.putExtra("IntentFrom", "CreateAccountActivity");
        startActivity(setupIntent);
        finish();
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}
