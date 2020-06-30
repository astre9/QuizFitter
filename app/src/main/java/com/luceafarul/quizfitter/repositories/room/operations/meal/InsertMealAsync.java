package com.luceafarul.quizfitter.repositories.room.operations.meal;

import android.content.Context;
import android.os.AsyncTask;

import com.luceafarul.quizfitter.model.Meal;
import com.luceafarul.quizfitter.repositories.room.DataBase;

public class InsertMealAsync extends AsyncTask<Meal, Void, Void> {

    private Context context;

    public InsertMealAsync(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Meal... meal) {
        DataBase dataBase = DataBase.getInstance(context);
        dataBase.mealsDao().insertMeal(meal[0]);
        return null;
    }
}