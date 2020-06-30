package com.luceafarul.quizfitter.model;

public class FoodDetails {
    public int calories;
    public int protein;
    public int carbohydrate;
    public int fat;

    public FoodDetails(int calories, int protein, int carbohydrate, int fat) {
        this.calories = calories;
        this.protein = protein;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
    }


    @Override
    public String toString() {
        return calories + "cal. protein " + protein + "gr. carbs " + carbohydrate + "gr. fat " + fat + "gr. ";
    }
}
