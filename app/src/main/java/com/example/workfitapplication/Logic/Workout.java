package com.example.workfitapplication.Logic;

import com.example.workfitapplication.Logic.Exercise;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Workout implements Serializable {
    private String name;
    private String workoutType;
    private String description;
    private ArrayList<Exercise> exercises = new ArrayList<>();
    private Date date;

    public Workout(String name) {
        this.name = name;
    }

    public Workout(){

    }

    public void addExercise(Exercise exercise){ this.exercises.add(exercise);}

    @Override
    public String toString(){ return name;}
}
