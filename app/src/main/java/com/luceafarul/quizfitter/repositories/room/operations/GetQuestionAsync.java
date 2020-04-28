package com.luceafarul.quizfitter.repositories.room.operations;


import android.content.Context;
import android.os.AsyncTask;

import com.luceafarul.quizfitter.models.Food;
import com.luceafarul.quizfitter.models.Question;
import com.luceafarul.quizfitter.models.RoomUser;
import com.luceafarul.quizfitter.models.User;
import com.luceafarul.quizfitter.repositories.room.DataBase;

import java.util.List;

public class GetQuestionAsync extends AsyncTask<Integer, Void, Question> {

    private Context context;

    public GetQuestionAsync(Context context) {
        this.context = context;
    }

    @Override
    protected Question doInBackground(Integer... ids) {
        return DataBase.getInstance(context).questionsDao().getQuestion(ids[0]);
    }
}