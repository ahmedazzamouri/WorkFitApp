package com.example.workfitapplication.Adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workfitapplication.Logic.Workout;
import com.example.workfitapplication.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class WorkoutAdapter extends FirestoreAdapter<WorkoutAdapter.ViewHolder>{


    public interface OnWorkoutSelectedListener {
        void onWorkoutSelected(DocumentSnapshot workout);
    }

    private OnWorkoutSelectedListener mListener;
    public WorkoutAdapter(Query query, OnWorkoutSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_workout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        public ViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.workout_item_name);
        }

        public void bind(DocumentSnapshot snapshot, OnWorkoutSelectedListener mListener) {

            Workout workout = snapshot.toObject(Workout.class);
            Resources resources = itemView.getResources();

            nameView.setText(workout.getName());

            itemView.setOnClickListener(view -> {
                if (mListener != null){
                    mListener.onWorkoutSelected(snapshot);
                }
            });


        }
    }
}
