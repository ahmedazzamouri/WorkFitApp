package com.example.workfitapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.workfitapplication.Adapter.WorkoutAdapter;
import com.example.workfitapplication.Logic.Exercise;
import com.example.workfitapplication.Logic.Workout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Objects;

public class WorkoutDetailActivity extends AppCompatActivity implements EventListener<DocumentSnapshot> {

    private ArrayList<String> exercises = new ArrayList<>();
    ListView Exercises;

    ArrayAdapter<String> exerciseAdapter;
    ArrayList<Exercise> exerciseList = new ArrayList<>();

    public static final String WORKOUT_ID = "workout_id";

    private TextView nameView;
    private TextView typeView;
    private TextView descView;
    private FirebaseFirestore mFirestore;
    private DocumentReference mWorkoutRef;
    private ListenerRegistration mWorkoutRegistration;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_workout);




        nameView = findViewById(R.id.workoutNameView);
        typeView = findViewById(R.id.workoutTypeView);
        descView = findViewById(R.id.workoutDescriptionView);

        Exercises = findViewById(R.id.exerciseListView);

        String workoutId = getIntent().getExtras().getString(WORKOUT_ID);
        if (workoutId == null) {
            throw new IllegalArgumentException("Must pass extra " + WORKOUT_ID);
        }

        mFirestore = FirebaseFirestore.getInstance();
        mWorkoutRef = mFirestore.collection("workouts").document(workoutId);




    }

    @Override
    public void onStart() {
        super.onStart();
        mWorkoutRegistration = mWorkoutRef.addSnapshotListener(this);
    }



    private void onWorkoutLoaded(Workout workout) {
        nameView.setText(workout.getName());
        typeView.setText(workout.getWorkoutType());
        descView.setText(workout.getDescription());
        exerciseList = workout.getExercises();
        for (Exercise exercise : exerciseList) {
            exercises.add(exercise.toString());
        }
        exerciseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, exercises);
        Exercises.setAdapter(exerciseAdapter);
    }

    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e){
        if (e!=null){
            Log.w("Workout Detail", "workout:onEvent", e);
            return;
        }
        onWorkoutLoaded(Objects.requireNonNull(snapshot.toObject(Workout.class)));
    }
}
