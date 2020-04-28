package com.luceafarul.quizfitter.repositories.room.daos;


import com.luceafarul.quizfitter.models.Food;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM Food WHERE userId = :userId")
    List<Food> getAll(int userId);

    @Query("SELECT * FROM Food WHERE name LIKE :foodName LIMIT 1")
    Food findByName(String foodName);

    @Insert
    public void insertFood(Food food);

    @Delete
    void delete(Food food);

    @Update
    void update(Food food);
}