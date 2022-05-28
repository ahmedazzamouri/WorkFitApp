package com.example.workfitapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workfitapplication.Logic.Exercise;
import com.example.workfitapplication.Logic.Workout;
import com.example.workfitapplication.preferences.PreferencesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class WorkoutFragment extends Fragment implements View.OnClickListener {

    ArrayList<Exercise> exercises = new ArrayList<>();

    ArrayList<String> exer = new ArrayList<>();

    View view;

    ListView Exercises;

    ArrayAdapter<String> exerciseAdapter;

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
            getActivity();
            if (resultCode == Activity.RESULT_OK){
                Exercise exercise = (Exercise) data.getSerializableExtra("exercise");
                exercises.add(exercise);
                exer.add(exercise.toString());
                LoadAdapterExercises();
            }
        }
    }

    private void LoadAdapterExercises() {
        exerciseAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, exer);
        Exercises.setAdapter(exerciseAdapter);
    }

    public Workout createWorkoutWithValues(){
        EditText typeWorkout = view.findViewById(R.id.worktoutType);
        EditText descWorkout = view.findViewById(R.id.workoutDescription);

        String name = nameWorkout.getText().toString();
        String type = typeWorkout.getText().toString();
        String desc = descWorkout.getText().toString();

        return new Workout(name,type, desc, exercises, new Date());

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.addExerciseButton){
            Intent exerciseIntent = new Intent(getActivity(), ExerciseActivity.class);
            startActivityForResult(exerciseIntent, 2);
        } else if (id == R.id.createWorkoutButton){

            if (!nameWorkout.getText().toString().equals("")) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Workout workout = createWorkoutWithValues();
                for (Exercise exercise : exercises){
                    workout.addExercise((exercise));
                }


                addWorkoutToFirebase(workout, db);

                Toast.makeText(getActivity(), "Workout added to Firestore", Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

            } else {
                Toast.makeText(getActivity(), "You have to introduce a name to your workout", Toast.LENGTH_LONG).show();
            }

        }

    }

    private void addWorkoutToFirebase(Workout workout, FirebaseFirestore db) {
        db.collection("workouts").document(workout.getName()).set(workout);
    }


    public boolean OnCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.config:
                startActivity(new Intent(getActivity(), PreferencesActivity.class));
                return true;

            case R.id.logout:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
