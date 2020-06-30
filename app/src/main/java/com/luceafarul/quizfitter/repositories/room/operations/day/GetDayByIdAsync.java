package com.luceafarul.quizfitter.repositories.room.operations.day;


import android.content.Context;
import android.os.AsyncTask;

import com.luceafarul.quizfitter.model.Day;
import com.luceafarul.quizfitter.repositories.room.DataBase;

import java.util.List;

public class GetDayByIdAsync extends AsyncTask<Integer, Void, Day> {

    private Context context;

    public GetDayByIdAsync(Context context) {
        this.context = context;
    }

    @Override
    protected Day doInBackground(Integer... ids) {
        return DataBase.getInstance(context).daysDao().getById(ids[0]);
    }
}