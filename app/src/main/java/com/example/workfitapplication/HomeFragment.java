package com.example.workfitapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workfitapplication.Adapter.WorkoutAdapter;
import com.example.workfitapplication.Logic.Workout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeFragment extends Fragment implements
WorkoutAdapter.OnWorkoutSelectedListener{


    RecyclerView workout;


    WorkoutAdapter workoutAdapter;

    private Query mQuery;

    private View errorView;


    private static final int LIMIT = 50;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mQuery = db.collection("workouts").orderBy("date", Query.Direction.DESCENDING).limit(LIMIT);

        workout = view.findViewById(R.id.workoutList);

        workoutAdapter = new WorkoutAdapter(mQuery, this);

        errorView = view.findViewById(android.R.id.content);

        initRecyclerView();

        return view;


    }

    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w("HomeFragment", "No query, not initializing RecyclerView");
        }

        workoutAdapter = new WorkoutAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    workout.setVisibility(View.GONE);
                } else {
                    workout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(errorView,
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        workout.setLayoutManager(new LinearLayoutManager(getActivity()));
        workout.setAdapter(workoutAdapter);
    }

    @Override
    public void onStart(){
        super.onStart();
        if (workoutAdapter != null){
            workoutAdapter.startListening();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if (workoutAdapter != null){
            workoutAdapter.stopListening();
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void onWorkoutSelected(DocumentSnapshot workout) {
        Intent intent = new Intent(getActivity(), WorkoutDetailActivity.class);
        intent.putExtra("workout_id", workout.getId());
        startActivity(intent);

    }
}