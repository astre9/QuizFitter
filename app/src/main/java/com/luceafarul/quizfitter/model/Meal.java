package com.luceafarul.quizfitter.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity(foreignKeys = @ForeignKey(entity = Day.class,
        parentColumns = "id",
        childColumns = "dayId",
        onDelete = ForeignKey.CASCADE))
public class Meal implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String imageName;
    private int dayId;

    @Ignore
    private List<Food> foodList;

    public Meal() {

    }

    public Meal(String name, int dayId) {
        this.name = name;
        this.dayId = dayId;
    }

    public Meal(String name, int dayId, String imageName) {
        this.name = name;
        this.dayId = dayId;
        this.imageName = imageName;
    }

    protected Meal(Parcel in) {
        id = in.readInt();
        name = in.readString();
        imageName = in.readString();
        dayId = in.readInt();
    }

    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setFoodList(List<Food> foodList) {
        if (foodList.size() > 0) {
            this.foodList = new ArrayList<>();
            this.foodList.addAll(foodList);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(imageName);
        dest.writeInt(dayId);
    }

    public static Meal[] populateData() {
        return new Meal[]{
                new Meal("Lunch", 1, "lunch1"),
                new Meal("Breakfast", 1, "nack"),
                new Meal("Brunch", 1, "brunch"),
                new Meal("Dinner", 2, "dinner"),
                new Meal("Snack", 3, "snach"),
                new Meal("Preworkout", 4, "preworkout"),
                new Meal("Snack", 5, "nack"),
                new Meal("Dinner", 6, "dinner2"),
        };
    }
}
