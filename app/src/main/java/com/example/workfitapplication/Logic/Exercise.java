package com.example.workfitapplication.Logic;

import com.example.workfitapplication.Logic.Enum.Type;

import java.io.Serializable;

public class Exercise implements Serializable {

    private String exercise;
    private int n_series;
    private int n_repetitions;
    private Type type;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public Exercise(){

    }

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

    public Exercise(String exercise, int n_series, int n_repetitions, Type type, String description){
        this.exercise = exercise;
        this.n_series = n_series;
        this.n_repetitions = n_repetitions;
        this.type = type;
        this.description = description;
    }

    public String getExercise() {
        return exercise;
    }

    @Override
    public String toString(){ return "Exercise name:" + exercise + " " + n_series + " " + "x" + n_repetitions + " " + "Type:"+ type.toString() + " " + "Description:"+ description;}
}
