package com.luceafarul.quizfitter.repositories.room.operations;


import android.content.Context;
import android.os.AsyncTask;

import com.luceafarul.quizfitter.models.Answer;
import com.luceafarul.quizfitter.models.Question;
import com.luceafarul.quizfitter.repositories.room.DataBase;

import java.util.List;

public class GetAnswersAsync extends AsyncTask<Integer, Void, List<Answer>> {

    private Context context;

    public GetAnswersAsync(Context context) {
        this.context = context;
    }

    @Override
    protected List<Answer> doInBackground(Integer... ids) {
        return DataBase.getInstance(context).answersDao().getAnswersByQuestionId(ids[0]);
    }
}