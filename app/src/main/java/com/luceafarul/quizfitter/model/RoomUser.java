package com.luceafarul.quizfitter.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RoomUser implements Parcelable {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "password")
    public String password;

    public static final Creator<RoomUser> CREATOR = new Creator<RoomUser>() {
        @Override
        public RoomUser createFromParcel(Parcel in) {
            return new RoomUser(in);
        }

        @Override
        public RoomUser[] newArray(int size) {
            return new RoomUser[size];
        }
    };

    public RoomUser(Parcel parcel) {
        uid = parcel.readInt();
        username = parcel.readString();
        password = parcel.readString();
    }

    public RoomUser() {

    }

    public int getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static Parcelable.Creator<RoomUser> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(uid);
        parcel.writeString(username);
        parcel.writeString(password);

    }
}