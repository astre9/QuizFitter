package com.luceafarul.quizfitter.repositories.room.operations.food;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.luceafarul.quizfitter.model.Food;
import com.luceafarul.quizfitter.repositories.room.DataBase;

public class UpdateFoodAsync extends AsyncTask<Food, Void, Void> {
    private Context context;

    public UpdateFoodAsync(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Food... foods) {
        DataBase dataBase = DataBase.getInstance(context);
        try {
            dataBase.foodsDAO().update(foods[0]);
        } catch (Exception e) {
            Log.e("Error updating food", e.getMessage());
        }
        return null;
    }

}
