package com.luceafarul.quizfitter.repositories.room.operations.food;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.luceafarul.quizfitter.model.Food;
import com.luceafarul.quizfitter.repositories.room.DataBase;

import java.util.List;

public class FoodRepository {
    private String DB_NAME = "db_task";

    private DataBase database;

    public FoodRepository(Context context) {
        database = DataBase.getInstance(context);
    }

    @SuppressLint("StaticFieldLeak")
    public void insertFood(final Food food) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.foodsDAO().insert(food);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateFood(final Food food) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.foodsDAO().update(food);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteFood(final Food food) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                database.foodsDAO().delete(food);
                return null;
            }
        }.execute();
    }

    public Food getFoodById(int id) {
        return database.foodsDAO().getById(id);
    }

    public List<Food> getAll() {
        return database.foodsDAO().getAll();
    }

    public List<Food> getAllByMeal(int mealId) {
        return database.foodsDAO().getAllByMeal(mealId);
    }
}
