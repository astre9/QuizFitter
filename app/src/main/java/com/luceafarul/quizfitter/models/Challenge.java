package com.luceafarul.quizfitter.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Challenge {

    public String starter;
    public String opponent;
    public String gameRef;

    public Challenge() {
        // required by Firebase database
    }

    public Challenge(String starter, String gameRef) {
        this.gameRef = gameRef;
        this.starter = starter;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "starter='" + starter + '\'' +
                ", opponent='" + opponent + '\'' +
                ", gameRef='" + gameRef + '\'' +
                '}';
    }
}

