package com.luceafarul.quizfitter.repositories.room.daos;

import com.luceafarul.quizfitter.models.Food;
import com.luceafarul.quizfitter.models.RoomUser;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    @Query("SELECT * FROM RoomUser WHERE username LIKE :username AND password LIKE :password LIMIT 1")
    RoomUser findByNamePassword(String username, String password);

    @Insert
    public void insertUser(RoomUser user);

    @Delete
    void delete(RoomUser user);

    @Update
    void update(RoomUser user);
}
