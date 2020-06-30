package com.luceafarul.quizfitter.repositories.room;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.luceafarul.quizfitter.model.Answer;
import com.luceafarul.quizfitter.model.Day;
import com.luceafarul.quizfitter.model.Food;
import com.luceafarul.quizfitter.model.Meal;
import com.luceafarul.quizfitter.model.Question;
import com.luceafarul.quizfitter.model.RoomUser;
import com.luceafarul.quizfitter.repositories.room.daos.AnswerDao;
import com.luceafarul.quizfitter.repositories.room.daos.DayDao;
import com.luceafarul.quizfitter.repositories.room.daos.FoodDao;
import com.luceafarul.quizfitter.repositories.room.daos.MealDao;
import com.luceafarul.quizfitter.repositories.room.daos.QuestionDao;
import com.luceafarul.quizfitter.repositories.room.daos.UserDao;

import java.util.concurrent.Executors;

@Database(entities = {Food.class, RoomUser.class, Question.class, Answer.class, Meal.class, Day.class}, version = 1, exportSchema = false)
public abstract class DataBase extends RoomDatabase {

    private static DataBase INSTANCE;

    public abstract FoodDao foodsDAO();

    public abstract UserDao usersDao();

    public abstract AnswerDao answersDao();

    public abstract QuestionDao questionsDao();

    public abstract MealDao mealsDao();

    public abstract DayDao daysDao();

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
                                getInstance(context).daysDao().insertAll(Day.populateData());
                                getInstance(context).mealsDao().insertAll(Meal.populateData());
                                getInstance(context).foodsDAO().insertAll(Food.populateData());
                            }
                        });
                    }
                })
                .build();

        db.query("select 1", null);

        return (DataBase) db;
    }
}