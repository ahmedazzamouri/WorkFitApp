package com.example.workfitapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workfitapplication.Logic.Exercise;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder> {
    private Context context;
    private List<Exercise> listExercise;

    public ExerciseAdapter(Context context, List<Exercise> listExercise) {
        this.context = context;
        this.listExercise = listExercise;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.exerciseName.setText(listExercise.get(position).getExercise());

    }

    @Override
    public int getItemCount() {
        return listExercise.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView exerciseName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.idData);
        }
    }
}
