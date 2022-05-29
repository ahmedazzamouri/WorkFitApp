package com.example.workfitapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workfitapplication.Logic.Enum.Type;
import com.example.workfitapplication.Logic.Exercise;
import com.example.workfitapplication.preferences.PreferencesActivity;

public class ExerciseActivity extends AppCompatActivity implements View.OnClickListener {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addexercise);

        Button createExercise = findViewById(R.id.createExerciseButton);

        createExercise.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        EditText nameExercise = findViewById(R.id.nameExercise);
        EditText n_series = findViewById(R.id.n_series);
        EditText n_repetitions = findViewById(R.id.n_repetitions);
        EditText descriptionExercise = findViewById(R.id.descExercise);
        Spinner exerciseType = findViewById(R.id.spinnerType);

        String name = nameExercise.getText().toString();
        int series = Integer.parseInt(n_series.getText().toString());
        int repetitions = Integer.parseInt(n_repetitions.getText().toString());
        String type = exerciseType.getSelectedItem().toString();
        String description = descriptionExercise.getText().toString();

        Type typeExercise = setType(type);

        Exercise exercise = new Exercise(name, series, repetitions, typeExercise, description);

        Intent intent = new Intent();
        intent.putExtra("exercise", exercise);
        setResult(RESULT_OK, intent);
        finish();


    }

    private Type setType(String type) {
        if (type.equals(Type.Biceps.toString())){
            return Type.Biceps;
        } else if (type.equals(Type.Triceps.toString())){
            return Type.Triceps;
        } else if (type.equals(Type.Chest.toString())){
            return Type.Chest;
        } else if (type.equals(Type.Back.toString())){
            return Type.Back;
        } else if (type.equals(Type.Legs.toString())){
            return Type.Legs;
        } else if (type.equals(Type.Abs.toString())){
            return Type.Abs;
        } else {
            return Type.Cardio;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.config) {
            startActivity(new Intent(this, PreferencesActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
