package com.luceafarul.quizfitter.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Match implements Parcelable {
    public String starter;
    public String opponent;
    public int starterScore;
    public int opponentScore;

    public Match() {
    }

    public Match(String starter) {
        this.starter = starter;
        this.starterScore = 0;
        this.opponentScore = 0;
    }

    protected Match(Parcel in) {
        starter = in.readString();
        opponent = in.readString();
        starterScore = in.readInt();
        opponentScore = in.readInt();
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    public void setScoreOfUser(String user, int score) {
        if (user.equals(this.starter)) {
            this.starterScore = score;
        } else {
            this.opponentScore = score;
        }
    }

    public int getScoreByUser(String user) {
        if (user.equals(this.starter)) {
            return this.starterScore;
        } else {
            return this.opponentScore;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(starter);
        dest.writeString(opponent);
        dest.writeInt(starterScore);
        dest.writeInt(opponentScore);
    }
}
