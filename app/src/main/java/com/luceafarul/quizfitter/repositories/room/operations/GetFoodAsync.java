package com.luceafarul.quizfitter.repositories.room.operations;


import android.content.Context;
import android.os.AsyncTask;

import com.luceafarul.quizfitter.models.Food;
import com.luceafarul.quizfitter.models.RoomUser;
import com.luceafarul.quizfitter.models.User;
import com.luceafarul.quizfitter.repositories.room.DataBase;

import java.util.List;

public class GetFoodAsync extends AsyncTask<RoomUser, Void, List<Food>> {

    private Context context;

    public GetFoodAsync(Context context) {
        this.context = context;
    }

    @Override
    protected List<Food> doInBackground(RoomUser... users) {

        return DataBase.getInstance(context).foodsDAO().getAll(users[0].uid);
    }
}