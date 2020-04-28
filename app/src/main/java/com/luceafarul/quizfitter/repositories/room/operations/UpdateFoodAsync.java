package com.luceafarul.quizfitter.repositories.room.operations;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.luceafarul.quizfitter.models.Food;
import com.luceafarul.quizfitter.repositories.room.DataBase;

public class UpdateFoodAsync extends AsyncTask<Food, Void, Void> {
    private Context context;

    public UpdateFoodAsync(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Food... lists) {
        DataBase dataBase = DataBase.getInstance(context);
        for (int i = 0; i < lists.length; i++) {
            try {
                dataBase.foodsDAO().update(lists[i]);
            } catch (Exception e) {
                Log.e("Error inserting new food", e.getMessage());
            }
        }
        return null;
    }

}