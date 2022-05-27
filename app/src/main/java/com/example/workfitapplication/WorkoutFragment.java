package com.example.workfitapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workfitapplication.Logic.Exercise;

import java.util.ArrayList;

public class WorkoutFragment extends Fragment implements View.OnClickListener {

    private ArrayList<Exercise> exercises = new ArrayList<>();

    private ArrayList<String> exer = new ArrayList<>();

    View view;

    RecyclerView Exercises;

    ExerciseAdapter exerciseAdapter;

    EditText nameWorkout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_addworkout, container, false);

        Exercises = view.findViewById(R.id.exerciseList);

        Button addExercise = view.findViewById(R.id.addExerciseButton);
        Button createWorkout = view.findViewById(R.id.createWorkoutButton);

        addExercise.setOnClickListener(this);
        createWorkout.setOnClickListener(this);

        nameWorkout = view.findViewById(R.id.nameWorkout);

        return view;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2){
            if (resultCode == getActivity().RESULT_OK){
                Exercise exercise = (Exercise) data.getSerializableExtra("exercise");
                exer.add(exercise.toString());
                LoadAdapterExercises();
            }
        }
    }

    private void LoadAdapterExercises() {
        Exercises.setLayoutManager(new LinearLayoutManager(view.getContext()));
        Exercises.setAdapter(exerciseAdapter);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.addExerciseButton){
            Intent exerciseIntent = new Intent(getActivity(), ExerciseActivity.class);
            startActivityForResult(exerciseIntent, 2);
        }

    }
}
