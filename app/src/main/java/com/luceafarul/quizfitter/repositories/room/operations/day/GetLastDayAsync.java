package com.luceafarul.quizfitter.repositories.room.operations.day;


import android.content.Context;
import android.os.AsyncTask;

import com.luceafarul.quizfitter.model.Day;
import com.luceafarul.quizfitter.repositories.room.DataBase;

import java.util.List;

public class GetLastDayAsync extends AsyncTask<Void, Void, Day> {

    private Context context;

    public GetLastDayAsync(Context context) {
        this.context = context;
    }

    @Override
    protected Day doInBackground(Void... voids) {
        return DataBase.getInstance(context).daysDao().getLastDay();
    }
}