package com.luceafarul.quizfitter.repositories.room.daos;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.luceafarul.quizfitter.model.Day;

import java.util.List;

@Dao
public interface DayDao {
    @Query("SELECT * FROM Day")
    List<Day> getAll();

    @Query("SELECT * FROM Day WHERE id = :id LIMIT 1")
    Day getById(int id);

    @Query("SELECT * FROM Day WHERE date LIKe :date LIMIT 1")
    Day getByDate(String date);

    @Query("SELECT * FROM Day ORDER BY id LIMIT 1")
    Day getLastDay();

    @Insert
    public long insert(Day day);

    @Insert
    public void insertAll(Day... days);

    @Delete
    void delete(Day day);

    @Update
    void update(Day day);
}