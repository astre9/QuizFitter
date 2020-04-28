package com.luceafarul.quizfitter.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.UUID;

public class UserDetails implements Parcelable {
    private String id;
    private String userId;
    private String date;
    private double bf;
    private double weight;

    public UserDetails() {
    }

    public UserDetails(String date, double bf, double weight, String userId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.date = date;
        this.bf = bf;
        this.weight = weight;
    }

    protected UserDetails(Parcel in) {
        id = in.readString();
        userId = in.readString();
        date = in.readString();
        bf = in.readDouble();
        weight = in.readDouble();
    }

    public static final Creator<UserDetails> CREATOR = new Creator<UserDetails>() {
        @Override
        public UserDetails createFromParcel(Parcel in) {
            return new UserDetails(in);
        }

        @Override
        public UserDetails[] newArray(int size) {
            return new UserDetails[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getBf() {
        return bf;
    }

    public void setBf(double bf) {
        this.bf = bf;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(date);
        dest.writeDouble(bf);
        dest.writeDouble(weight);
    }
}
