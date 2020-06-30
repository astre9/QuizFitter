package com.luceafarul.quizfitter.repositories.room.daos;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.luceafarul.quizfitter.model.Meal;

import java.util.List;

@Dao
public interface MealDao {
    @Query("SELECT * FROM Meal")
    List<Meal> getAll();

    @Query("SELECT * FROM Meal WHERE dayId=:dayId")
    List<Meal> getMealsByDay(int dayId);

    @Query("SELECT * FROM Meal WHERE name LIKE :mealName LIMIT 1")
    Meal findByName(String mealName);

    @Insert
    public long insertMeal(Meal meal);

    @Insert
    void insertAll(Meal... meals);

    @Delete
    void delete(Meal meal);

    @Update
    void update(Meal meal);
}