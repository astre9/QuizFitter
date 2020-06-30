package com.luceafarul.quizfitter.repositories.room.operations.food;


import android.content.Context;
import android.os.AsyncTask;

import com.luceafarul.quizfitter.model.Food;

import java.util.List;

public class GetFoodAsync extends AsyncTask<String, Void, List<Food>> {

    private Context context;

    public GetFoodAsync(Context context) {
        this.context = context;
    }

    @Override
    protected List<Food> doInBackground(String... ids) {

        return null;//DataBase.getInstance(context).foodsDAO().getAll();
    }
}