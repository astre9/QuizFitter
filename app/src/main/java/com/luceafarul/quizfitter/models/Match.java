package com.luceafarul.quizfitter.models;

import androidx.annotation.Nullable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Match {
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
}
