package com.luceafarul.quizfitter.repositories.room.operations;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.luceafarul.quizfitter.models.RoomUser;
import com.luceafarul.quizfitter.repositories.room.DataBase;

public class InsertUserAsync extends AsyncTask<RoomUser, Void, Void> {

    private Context context;

    public InsertUserAsync(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(RoomUser... lists) {
        DataBase dataBase = DataBase.getInstance(context);
        for (RoomUser list : lists) {
            try {
                dataBase.usersDao().insertUser(list);
            } catch (Exception e) {
                Log.e("Error inserting new user", e.getMessage());
            }
        }
        return null;
    }
}