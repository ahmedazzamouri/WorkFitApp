package com.example.workfitapplication.Logic;


import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Workout implements Serializable{
    private String name;
    private String workoutType;
    private String description;
    private ArrayList<Exercise> exercises = new ArrayList<>();
    private Date date;

    public Workout() {
    }

    public Workout(String name, String workoutType, String description, ArrayList<Exercise> exercises, Date date) {
        this.name = name;
        this.workoutType = workoutType;
        this.description = description;
        this.exercises = exercises;
        this.date = date;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void addExercise(Exercise exercise){ this.exercises.add(exercise);}

    @NonNull

    @Override
    public String toString() {
        return "Workout{" +
                "name='" + name + '\'' +
                ", workoutType='" + workoutType + '\'' +
                ", description='" + description + '\'' +
                ", exercises=" + exercises +
                ", date=" + date +
                '}';
    }
}
