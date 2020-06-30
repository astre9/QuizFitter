package com.luceafarul.quizfitter.repositories.room.operations.day;


import android.content.Context;
import android.os.AsyncTask;

import com.luceafarul.quizfitter.model.Answer;
import com.luceafarul.quizfitter.model.Day;
import com.luceafarul.quizfitter.repositories.room.DataBase;

import java.util.List;

public class GetDaysAsync extends AsyncTask<Void, Void, List<Day>> {

    private Context context;

    public GetDaysAsync(Context context) {
        this.context = context;
    }

    @Override
    protected List<Day> doInBackground(Void... voids) {
        return DataBase.getInstance(context).daysDao().getAll();
    }
}