package com.example.workfitapplication.Logic;

import com.example.workfitapplication.Logic.Enum.Type;

import java.io.Serializable;

public class Exercise implements Serializable {

    private String exercise;
    private int n_series;
    private int n_repetitions;
    private Type type;

    public int getN_series() {
        return n_series;
    }

    public void setN_series(int n_series) {
        this.n_series = n_series;
    }

    public int getN_repetitions() {
        return n_repetitions;
    }

    public void setN_repetitions(int n_repetitions) {
        this.n_repetitions = n_repetitions;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public Exercise(String exercise, int n_series, int n_repetitions, Type type){
        this.exercise = exercise;
        this.n_series = n_series;
        this.n_repetitions = n_repetitions;
        this.type = type;
    }

    public String getExercise() {
        return exercise;
    }

    @Override
    public String toString(){ return exercise + " " + n_series + " " + "x" + n_repetitions + " " + type.toString();}
}
