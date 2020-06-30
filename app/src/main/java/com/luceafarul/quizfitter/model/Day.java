package com.luceafarul.quizfitter.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity
public class Day {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String date;
    private String comment;
    private String imageName;
    private int calories;
    private String userId;

    public Day() {
    }

    public Day(String date, String userId) {
        this.date = date;
        this.userId = userId;
    }

    @Ignore
    public Day(String date, String userId, String imageName) {
        this.date = date;
        this.userId = userId;
        this.imageName = imageName;
    }

    public int getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static Day[] populateData() {
        return new Day[]{
                new Day("Mon, 1 June 2020", "tt37aNOSF2dzMiog5spucHsYcGj2", "lunch1"),
                new Day("Sun, 31 May 2020", "tt37aNOSF2dzMiog5spucHsYcGj2", "dinner"),
                new Day("Sat, 30 May 2020", "tt37aNOSF2dzMiog5spucHsYcGj2", "snach"),
                new Day("Fri, 29 May 2020", "tt37aNOSF2dzMiog5spucHsYcGj2", "preworkout"),
                new Day("Thu, 28 May 2020", "tt37aNOSF2dzMiog5spucHsYcGj2", "nack"),
                new Day("Wed, 27 May 2020", "tt37aNOSF2dzMiog5spucHsYcGj2", "dinner2"),
        };
    }
}
