package com.luceafarul.quizfitter.repositories.room.daos;


import com.luceafarul.quizfitter.model.Food;
import com.luceafarul.quizfitter.model.FoodDetails;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM Food")
    List<Food> getAll();

    @Query("SELECT * FROM Food WHERE fid = :id")
    Food getById(int id);

    @Query("SELECT * FROM Food WHERE mealId == :mealId")
    List<Food> getAllByMeal(int mealId);

    @Query("SELECT SUM(calories) AS calories, SUM(protein) AS protein, SUM(carbohydrate) AS carbohydrate, SUM(fat) AS fat FROM Food WHERE mealId == :mealId")
    FoodDetails getDetailsByMeal(int mealId);

    @Insert
    public void insert(Food food);

    @Insert
    void insertAll(Food... foods);

    @Delete
    void delete(Food food);

    @Update
    void update(Food food);
}