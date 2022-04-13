package com.example.workfitapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CalorieRecommendation extends AppCompatActivity {

    EditText age, height, weight;
    Spinner gender;
    Button calc_bttn;
    TextView maintenance, bulk, cut, protein, carbs, fats;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_recommendation);

        age = findViewById(R.id.editAge);
        height = findViewById(R.id.editHeight);
        weight = findViewById(R.id.editWeight);
        gender = findViewById(R.id.gender);
        calc_bttn = findViewById(R.id.bttn_calc);
        maintenance = findViewById(R.id.cal_maintain);
        cut = findViewById(R.id.cal_cut);
        bulk = findViewById(R.id.cal_bulk);
        protein = findViewById(R.id.macros_protein);
        carbs = findViewById(R.id.macros_carbs);
        fats = findViewById(R.id.macros_fat);

        calc_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = gender.getSelectedItem().toString();
                if (s.equalsIgnoreCase("Male")){
                    maleCalc();
                } else if (s.equalsIgnoreCase("Female")){
                    femaleCalc();
                } else {
                    noCalc();
                }
            }
        });
    }

    private void noCalc(){
        maintenance.setText(null);
        bulk.setText(null);
        cut.setText(null);
        protein.setText(null);
        carbs.setText(null);
        fats.setText(null);
    }

    private void maleCalc(){
        double weight_user = Double.parseDouble(weight.getText().toString());
        double height_user = Double.parseDouble(height.getText().toString());
        double age_user = Double.parseDouble(age.getText().toString());
        double formula, formula2, formula4, formula5, formula6, result, bulk_cal, cut_cal, protein_gr, carbs_gr, fats_gr;
        formula2 = 5 * height_user;
        formula4 = 6.8 * age_user;
        formula5 = formula2/formula4;
        formula6 = 13.8 * weight_user;
        formula = 665.1 + formula6 + formula5;
        result = formula * 1.3;
        maintenance.setText(String.format("%00f",result));
        bulk_cal = result + 500;
        bulk.setText(String.format("%00f", bulk_cal));
        cut_cal = result - 500;
        cut.setText(String.format("%00f", cut_cal));
        protein_gr = (0.4 * result) / 4;
        protein.setText(String.format("%.00f", protein_gr));
        carbs_gr = (0.6 * result) / 4;
        carbs.setText(String.format("%.00f", carbs_gr));
        fats_gr = (0.2 * result) / 9;
        fats.setText(String.format("%.00f", fats_gr));

    }

    private void femaleCalc() {
        double weight_user = Double.parseDouble(weight.getText().toString());
        double height_user = Double.parseDouble(height.getText().toString());
        double age_user = Double.parseDouble(age.getText().toString());
        double formula, formula2, formula4, formula5, formula6, result, bulk_cal, cut_cal, protein_gr, carbs_gr, fats_gr;
        formula2 = 1.9 * height_user;
        formula4 = 4.7 * age_user;
        formula5 = formula2 / formula4;
        formula6 = 9.6 * weight_user;
        formula = 665.1 + formula6 + formula5;
        result = formula * 1.3;
        maintenance.setText(String.format("%.00f", result));
        bulk_cal = result + 400;
        bulk.setText(String.format("%.00f", bulk_cal));
        cut_cal = result - 400;
        cut.setText(String.format("%.00f", cut_cal));
        protein_gr = (0.4 * result) / 4;
        protein.setText(String.format("%.00f", protein_gr));
        carbs_gr = (0.6 * result) / 4;
        carbs.setText(String.format("%.00f", carbs_gr));
        fats_gr = (0.2 * result) / 9;
        fats.setText(String.format("%.00f", fats_gr));
    }

    protected void onStop(){
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();

        ed.putString("age", age.toString());
        ed.putString("height", height.toString());
        ed.putString("weight", weight.toString());
        ed.putString("gender", gender.getSelectedItem().toString());

        ed.apply();
    }
}
