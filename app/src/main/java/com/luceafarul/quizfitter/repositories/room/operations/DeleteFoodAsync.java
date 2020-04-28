package com.luceafarul.quizfitter.repositories.room.operations;

import android.content.Context;
import android.os.AsyncTask;

import com.luceafarul.quizfitter.models.Food;
import com.luceafarul.quizfitter.models.RoomUser;
import com.luceafarul.quizfitter.repositories.room.DataBase;

public class DeleteFoodAsync extends AsyncTask<Food, Void, Void> {
    private Context context;

    public DeleteFoodAsync(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Food... food) {
        DataBase.getInstance(context).foodsDAO().delete(food[0]);
        return null;
    }
}
