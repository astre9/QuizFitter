package com.luceafarul.quizfitter.repositories.room.operations;

import android.content.Context;
import android.os.AsyncTask;

import com.luceafarul.quizfitter.model.RoomUser;
import com.luceafarul.quizfitter.repositories.room.DataBase;

public class DeleteUserAsync extends AsyncTask<RoomUser, Void, Void> {
    private Context context;

    public DeleteUserAsync(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(RoomUser... users) {
        DataBase.getInstance(context).usersDao().delete(users[0]);
        return null;
    }
}
