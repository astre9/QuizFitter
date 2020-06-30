package com.luceafarul.quizfitter.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Queue implements Parcelable {
    private String id;
    private String matchId;
    private String userId;

    public Queue() {
    }

    public Queue(String userId) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.matchId = this.id;
        this.userId = userId;
    }

    protected Queue(Parcel in) {
        id = in.readString();
        matchId = in.readString();
        userId = in.readString();
    }

    public static final Creator<Queue> CREATOR = new Creator<Queue>() {
        @Override
        public Queue createFromParcel(Parcel in) {
            return new Queue(in);
        }

        @Override
        public Queue[] newArray(int size) {
            return new Queue[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(matchId);
        parcel.writeString(userId);
    }
}
