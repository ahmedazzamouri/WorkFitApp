package com.example.workfitapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;

public class DaySplits extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    RadioGroup radioGroup;
    RadioButton rb1, rb2, rb3;
    Button go;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daysplits);
        radioGroup = findViewById(R.id.radiogroup1);
        rb1 = findViewById(R.id.bttn1);
        rb2 = findViewById(R.id.bttn2);
        rb3 = findViewById(R.id.bttn3);
        go = findViewById(R.id.go_bttn);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb1.isChecked()){
                    Intent intent = new Intent(getApplicationContext(),ThreeDaySplitActivity.class);
                    startActivity(intent);
                } else if (rb2.isChecked()){
                    Intent intent = new Intent(getApplicationContext(),FourDaySplitActivity.class);
                    startActivity(intent);
                } else if (rb3.isChecked()){
                    Intent intent = new Intent(getApplicationContext(),FiveDaySplitActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_timer){
            Intent intent1 = new Intent(getApplicationContext(), Timer.class);
            startActivity(intent1);
        }
        else if (id == R.id.nav_profile){
            Intent intent2 = new Intent(getApplicationContext(), UserProfileFragment.class);
            startActivity(intent2);
        }
        else if (id == R.id.nav_splits){
            Intent intent3 = new Intent(getApplicationContext(), DaySplits.class);
            startActivity(intent3);
        } else if (id == R.id.nav_calc_calorie){
            Intent intent4 = new Intent(getApplicationContext(), CalorieRecommendation.class);
            startActivity(intent4);
        } else if (id == R.id.nav_steps){
            Intent intent5 = new Intent(getApplicationContext(),StepCountActivity.class);
            startActivity(intent5);
        }
        return true;
    }
}
