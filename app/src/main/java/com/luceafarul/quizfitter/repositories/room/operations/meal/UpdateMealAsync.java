package com.luceafarul.quizfitter.repositories.room.operations.meal;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.luceafarul.quizfitter.model.Meal;
import com.luceafarul.quizfitter.repositories.room.DataBase;

public class UpdateMealAsync extends AsyncTask<Meal, Void, Void> {
    private Context context;

    public UpdateMealAsync(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Meal... meals) {
        DataBase dataBase = DataBase.getInstance(context);
        try {
            dataBase.mealsDao().update(meals[0]);
        } catch (Exception e) {
            Log.e("Error updating meal", e.getMessage());
        }
        return null;
    }

}
