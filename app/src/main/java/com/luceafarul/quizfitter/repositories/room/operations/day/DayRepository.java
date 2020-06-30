package com.luceafarul.quizfitter.repositories.room.operations.day;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.luceafarul.quizfitter.model.Day;
import com.luceafarul.quizfitter.model.Food;
import com.luceafarul.quizfitter.repositories.room.DataBase;

import java.util.List;

public class DayRepository {

    private DataBase database;
    private static DayRepository instance = null;

    public DayRepository(Context context) {
        database = DataBase.getInstance(context);
    }

    public static DayRepository getInstance(Context context) {
        if (instance == null) {
            instance = new DayRepository(context);
        }
        return instance;
    }

    @SuppressLint("StaticFieldLeak")
    public void insertDay(final Day day) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.daysDao().insert(day);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateDay(final Day day) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.daysDao().update(day);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteDay(final Food food) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.foodsDAO().delete(food);
                return null;
            }
        }.execute();
    }

//    public LiveData<Day> getLastDay() {
//        return database.daysDao().getLastDay();
//    }
}
