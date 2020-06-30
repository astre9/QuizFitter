package com.luceafarul.quizfitter.repositories.room.daos;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.luceafarul.quizfitter.model.Answer;

import java.util.List;

@Dao
public interface AnswerDao {
    @Query("SELECT * FROM Answer")
    List<Answer> getAll();

    @Query("SELECT * FROM Answer WHERE questionId = :id")
    List<Answer> getAnswersByQuestionId(int id);

    @Insert
    void insertAll(Answer... answers);

}