package com.luceafarul.quizfitter.repositories.room.operations.food;


import android.content.Context;
import android.os.AsyncTask;

import com.luceafarul.quizfitter.model.Food;
import com.luceafarul.quizfitter.repositories.room.DataBase;

import java.util.List;

public class GetTotalCaloriesAsync extends AsyncTask<String, Void, Integer> {

    private Context context;

    public GetTotalCaloriesAsync(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String... ids) {

        return DataBase.getInstance(context).daysDao().getTotalCalories(ids[0]);
    }
}