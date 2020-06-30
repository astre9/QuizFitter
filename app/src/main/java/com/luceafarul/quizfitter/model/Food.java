package com.luceafarul.quizfitter.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Meal.class,
        parentColumns = "id",
        childColumns = "mealId",
        onDelete = ForeignKey.CASCADE))
public class Food implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int fid;
    private String name;
    private int quantity;
    private int calories;
    private int protein;
    private int carbohydrate;
    private int fat;
    private int mealId;

    @Ignore
    public Food(String name, int quantity, int calories, int mealId) {
        this.name = name;
        this.quantity = quantity;
        this.calories = calories;
        this.mealId = mealId;
    }

    public Food(String name, int calories, int quantity, int mealId, int protein, int fat, int carbohydrate) {
        this.name = name;
        this.calories = calories;
        this.quantity = quantity;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
        this.mealId = mealId;
    }

    protected Food(Parcel in) {
        fid = in.readInt();
        name = in.readString();
        calories = in.readInt();
        protein = in.readInt();
        carbohydrate = in.readInt();
        fat = in.readInt();
        mealId = in.readInt();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public void setFid(int fid) {
        this.fid = fid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public int getFid() {
        return fid;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public int getProtein() {
        return protein;
    }

    public int getCarbohydrate() {
        return carbohydrate;
    }

    public int getFat() {
        return fat;
    }

    public int getMealId() {
        return mealId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(fid);
        dest.writeString(name);
        dest.writeInt(calories);
        dest.writeInt(protein);
        dest.writeInt(carbohydrate);
        dest.writeInt(fat);
        dest.writeInt(mealId);
    }

    //    public Food(String name, int calories, int quantity, int mealId, int protein, int fat, int carbohydrate) {
    public static Food[] populateData() {
        return new Food[]{
                new Food("Potatoes", 225, 300, 1, 6, 1, 51),
                new Food("Tomatoes", 22, 123, 1, 1, 1, 5),
                new Food("Chicken breast", 198, 120, 1, 37, 5, 0),
                new Food("Salmon Sandwich", 810, 278, 2, 36, 59, 33),
                new Food("Greek Yogurt", 100, 170, 2, 17, 1, 7),
                new Food("Cake", 262, 67, 2, 2, 12, 38),
                new Food("Cake", 262, 67, 4, 2, 12, 38),
                new Food("Medium Bagel", 280, 105, 5, 11, 2, 55),
                new Food("Medium Bagel", 280, 105, 6, 11, 2, 55),
                new Food("Potatoes", 225, 300, 7, 6, 1, 51),
                new Food("Potatoes", 225, 300, 8, 6, 1, 51),
        };
    }
}