package com.example.workfitapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {

    private View view;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseReference;
    private CircleImageView profileUserImage;
    String currentUserId;
    String userImage, username, joinedDate, weight, height, phonenumber, email, name;
    private TextView profileUsername, profileJoinedDate, profileWeight, profileHeight, profileEmail, profilePhoneNumber, profileName;

    public UserProfileFragment() {
        //Required Empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        userDatabaseReference = FirebaseDatabase.getInstance("https://workfitapplication-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Users").child(currentUserId);

        profileUserImage = view.findViewById(R.id.profile_user_image);
        profileUsername = view.findViewById(R.id.profile_username);
        profileName = view.findViewById(R.id.profile_name);
        profileJoinedDate = view.findViewById(R.id.profile_joined_date);
        profileEmail = view.findViewById(R.id.profile_email);
        profileHeight = view.findViewById(R.id.profile_height);
        profileWeight = view.findViewById(R.id.profile_weight);
        profilePhoneNumber = view.findViewById(R.id.profile_phone);

        RetrieveUserDetails();

        return view;
    }
    
    @Override
    public void onResume(){
        super.onResume();
        buildTitle();
    }

    private void buildTitle() {
        TextView name = getActivity().findViewById(R.id.profile_name);
        TextView email = getActivity().findViewById(R.id.profile_email);
        TextView username = getActivity().findViewById(R.id.profile_username);

        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String alias = mySharedPreferences.getString(getString(R.string.name), "No name");
        String mail = mySharedPreferences.getString(getString(R.string.email), "No email");
        String accountname = mySharedPreferences.getString(getString(R.string.username), "No username");

        name.setText(alias);
        email.setText(mail);
        username.setText(accountname);
    }

    private void RetrieveUserDetails() {

        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.hasChild("userimage")){
                        userImage = dataSnapshot.child("userimage").getValue().toString();
                        if (!TextUtils.isEmpty(userImage)){
                            Picasso.get().load(userImage).placeholder(R.drawable.profile_photo).into(profileUserImage);
                        }
                    }

                    if (dataSnapshot.hasChild("username"));{
                        username = dataSnapshot.child("username").getValue().toString();
                        if (!TextUtils.isEmpty(username)){
                            profileUsername.setText(username);
                        }
                    }

                    if (dataSnapshot.hasChild("useremail"));{
                        email = dataSnapshot.child("useremail").getValue().toString();
                        if (!TextUtils.isEmpty(email)){
                            profileEmail.setText(email);
                        }
                    }

                    if (dataSnapshot.hasChild("userjoineddate"));{
                        joinedDate = dataSnapshot.child("userjoineddate").getValue().toString();
                        if (!TextUtils.isEmpty(joinedDate)){
                            profileJoinedDate.setText("Joined "+joinedDate);
                        }
                    }

                    if (dataSnapshot.hasChild("user_fullname"));{
                        name = dataSnapshot.child("user_fullname").getValue().toString();
                        if (!TextUtils.isEmpty(name)){
                            profileName.setText(name);
                        }
                    }

                    if (dataSnapshot.hasChild("user_phone"));{
                        phonenumber = dataSnapshot.child("user_phone").getValue().toString();
                        if (!TextUtils.isEmpty(phonenumber)){
                            profilePhoneNumber.setText(phonenumber);
                        }

                    }

                    if (dataSnapshot.hasChild("user_weight")){
                        weight = dataSnapshot.child("user_weight").getValue().toString();
                        if (!TextUtils.isEmpty(weight)){
                            profileWeight.setText(" • Weight -  "+weight+" kg •");
                        }
                    }

                    if (dataSnapshot.hasChild("user_height")){
                        height = dataSnapshot.child("user_height").getValue().toString();
                        if (!TextUtils.isEmpty(height)){
                            profileHeight.setText(" • Height - "+height+" cm •");
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
