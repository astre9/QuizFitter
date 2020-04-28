package com.luceafarul.quizfitter.models;

public class Exercise {
    private String id;
    private String category;
    private String imageName;
    private String name;
    private String day;
    private String Details;
    private int difficulty;
    private int reps;
    private int sets;

    public Exercise() {
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String Details) {
        this.Details = Details;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public Exercise(String id, String category, String imageName, String name, String day, int difficulty, int reps, int sets) {
        this.id = id;
        this.category = category;
        this.imageName = imageName;
        this.name = name;
        this.day = day;
        this.difficulty = difficulty;
        this.reps = reps;
        this.sets = sets;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }

    public void setiImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
