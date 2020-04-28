package com.luceafarul.quizfitter.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Challenge {

    public String starter;
    public String opponent;
    public String gameRef;
    public int starterScore;
    public int opponentScore;

    public Challenge() {
        // required by Firebase database
    }

    public Challenge(String opener, String gameRef) {
        this.gameRef = gameRef;
        this.starter = opener;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public void setStarter(String starter) {
        this.starter = starter;
    }

    public void setScore(String user, int score) {
        if (user.equals(this.starter)) {
            this.starterScore = score;
        } else {
            this.opponentScore = score;
        }
    }

    public int getScore(String user) {
        if (user.equals(this.starter)) {
            return this.starterScore;
        } else {
            return this.opponentScore;
        }
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "starter='" + starter + '\'' +
                ", opponent='" + opponent + '\'' +
                ", gameRef='" + gameRef + '\'' +
                ", starterScore=" + starterScore +
                ", opponentScore=" + opponentScore +
                '}';
    }
}

