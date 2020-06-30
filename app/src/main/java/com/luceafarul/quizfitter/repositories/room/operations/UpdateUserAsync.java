package com.luceafarul.quizfitter.repositories.room.operations;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.luceafarul.quizfitter.model.RoomUser;
import com.luceafarul.quizfitter.repositories.room.DataBase;


public class UpdateUserAsync extends AsyncTask<RoomUser, Void, Void> {
    private Context context;

    public UpdateUserAsync(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(RoomUser... lists) {
        DataBase dataBase = DataBase.getInstance(context);
        for (int i = 0; i < lists.length; i++) {
            try {
                dataBase.usersDao().update(lists[i]);
            } catch (Exception e) {
                Log.e("Error inserting new food", e.getMessage());
            }
        }
        return null;
    }

}
