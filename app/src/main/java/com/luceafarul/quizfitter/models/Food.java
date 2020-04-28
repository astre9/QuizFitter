package com.luceafarul.quizfitter.models;


import com.luceafarul.quizfitter.others.TimestampConverter;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = RoomUser.class,
        parentColumns = "uid",
        childColumns = "userId",
        onDelete = CASCADE))
public class Food {
    @PrimaryKey
    public int fid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "calories")
    public int calories;

    @ColumnInfo(name = "protein")
    public int protein;

    @ColumnInfo(name = "carb")
    public int carb;

    @ColumnInfo(name = "fat")
    public int fat;
//
//    @ColumnInfo(name = "date")
//    @TypeConverters({TimestampConverter.class})
//    public Date date;

    @ColumnInfo(name = "userId")
    public int userId;


}