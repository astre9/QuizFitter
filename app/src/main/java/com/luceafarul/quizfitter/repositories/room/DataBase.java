package com.luceafarul.quizfitter.repositories.room;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.luceafarul.quizfitter.models.Answer;
import com.luceafarul.quizfitter.models.Food;
import com.luceafarul.quizfitter.models.Question;
import com.luceafarul.quizfitter.models.RoomUser;
import com.luceafarul.quizfitter.repositories.room.daos.AnswerDao;
import com.luceafarul.quizfitter.repositories.room.daos.FoodDao;
import com.luceafarul.quizfitter.repositories.room.daos.QuestionDao;
import com.luceafarul.quizfitter.repositories.room.daos.UserDao;

import java.sql.RowId;
import java.util.concurrent.Executors;

import javax.security.auth.callback.Callback;

@Database(entities = {Food.class, RoomUser.class, Question.class, Answer.class}, version = 1, exportSchema = false)
public abstract class DataBase extends RoomDatabase {

    private static DataBase INSTANCE;

    public abstract FoodDao foodsDAO();

    public abstract UserDao usersDao();

    public abstract AnswerDao answersDao();

    public abstract QuestionDao questionsDao();

    public synchronized static DataBase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static DataBase buildDatabase(final Context context) {
        RoomDatabase db = Room.databaseBuilder(context,
                DataBase.class,
                "quizzfitter.db")
                .addCallback(new Callback() {
                    public void onCreate(SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                getInstance(context).questionsDao().insertAll(Question.populateData());
                                getInstance(context).answersDao().insertAll(Answer.populateData());
                            }
                        });
                    }
                })
                .build();

        db.query("select 1", null);

        return (DataBase) db;
    }
}