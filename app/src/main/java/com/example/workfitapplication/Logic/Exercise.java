package com.example.workfitapplication.Logic;

import com.example.workfitapplication.Logic.Enum.Type;

import java.io.Serializable;

public class Exercise implements Serializable {

    private String exercise;
    private int n_series;
    private int n_repetitions;
    private Type type;

    public Exercise(String exercise, int n_series, int n_repetitions,Type type){
        this.exercise = exercise;
        this.n_series = n_series;
        this.n_repetitions = n_repetitions;
        this.type = type;
    }

    public String getExercise() {
        return exercise;
    }

    @Override
    public String toString(){ return exercise + " " + n_series + " " + n_repetitions + " " + type;}
}
