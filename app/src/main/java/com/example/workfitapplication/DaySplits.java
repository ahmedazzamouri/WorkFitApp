package com.example.workfitapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class DaySplits extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton rb1, rb2, rb3, rb4;
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
}
