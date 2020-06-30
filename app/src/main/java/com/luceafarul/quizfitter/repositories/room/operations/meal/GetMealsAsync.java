package com.luceafarul.quizfitter.repositories.room.operations.meal;


import android.content.Context;
import android.os.AsyncTask;

import com.luceafarul.quizfitter.model.Day;
import com.luceafarul.quizfitter.model.Meal;
import com.luceafarul.quizfitter.repositories.room.DataBase;

import java.util.List;

public class GetMealsAsync extends AsyncTask<Void, Void, List<Meal>> {

    private Context context;

    public GetMealsAsync(Context context) {
        this.context = context;
    }

    @Override
    protected List<Meal> doInBackground(Void... voids) {
        return DataBase.getInstance(context).mealsDao().getAll();
    }
}