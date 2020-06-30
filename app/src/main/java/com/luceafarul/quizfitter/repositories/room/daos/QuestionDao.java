package com.luceafarul.quizfitter.repositories.room.daos;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.luceafarul.quizfitter.model.Question;

import java.util.List;

@Dao
public interface QuestionDao {
    @Query("SELECT * FROM Question")
    List<Question> getAll();

    @Query("SELECT * FROM Question WHERE id = :id")
    Question getQuestion(int id);

    @Insert
    void insertAll(Question... questions);

}