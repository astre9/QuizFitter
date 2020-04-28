package com.luceafarul.quizfitter.models;

public class Match {
    private String id;

    public Match() {
    }

    public Match(String id, String firstUserId, String secondUserId) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
