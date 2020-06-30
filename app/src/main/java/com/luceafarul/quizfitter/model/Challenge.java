package com.luceafarul.quizfitter.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Challenge {

    public String starter;
    public String opponent;
    public String matchRef;

    public Challenge() {
        // required by Firebase database
    }

    public Challenge(String starter, String matchRef) {
        this.matchRef = matchRef;
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
                ", gameRef='" + matchRef + '\'' +
                '}';
    }
}

