package com.luceafarul.quizfitter.repositories.room.operations.food;

import android.content.Context;
import android.os.AsyncTask;

import com.luceafarul.quizfitter.model.Food;
import com.luceafarul.quizfitter.repositories.room.DataBase;

public class InsertFoodAsync extends AsyncTask<Food, Void, Void> {

    private Context context;

    public InsertFoodAsync(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Food... food) {
        DataBase dataBase = DataBase.getInstance(context);
        dataBase.foodsDAO().insert(food[0]);
        return null;
    }
}