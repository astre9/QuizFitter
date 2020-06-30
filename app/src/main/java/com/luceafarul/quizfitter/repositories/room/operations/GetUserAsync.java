package com.luceafarul.quizfitter.repositories.room.operations;

import android.content.Context;
import android.os.AsyncTask;

import com.luceafarul.quizfitter.model.RoomUser;
import com.luceafarul.quizfitter.repositories.room.DataBase;

public class GetUserAsync extends AsyncTask<RoomUser, Void, RoomUser> {

    private Context context;

    public GetUserAsync(Context context) {
        this.context = context;
    }

    @Override
    protected RoomUser doInBackground(RoomUser... RoomUser) {
        String username = RoomUser[0].username;
        String password = RoomUser[0].password;
        return DataBase.getInstance(context).usersDao().findByNamePassword(username, password);
    }
}