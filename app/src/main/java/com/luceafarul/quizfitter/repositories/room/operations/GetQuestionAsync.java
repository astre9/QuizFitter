package com.luceafarul.quizfitter.repositories.room.operations;


import android.content.Context;
import android.os.AsyncTask;

import com.luceafarul.quizfitter.model.Question;
import com.luceafarul.quizfitter.repositories.room.DataBase;

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