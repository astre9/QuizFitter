package com.luceafarul.quizfitter.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Answer {
    @PrimaryKey
    private int id;
    private String text;
    private boolean isCorrect;
    private int questionId;

    public Answer(int id, String text, int questionId, boolean isCorrect) {
        this.id = id;
        this.text = text;
        this.isCorrect = isCorrect;
        this.questionId = questionId;
    }

    public Answer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public static Answer[] populateData() {
        return new Answer[]{
                new Answer(1, "Less efficient for sports goals.", 1, false),
                new Answer(2, "Hard to focus on a weaker muscle group.", 1, true),
                new Answer(3, "Inability to catch up if missing a workout.", 1, false),
                new Answer(4, "False", 2, true),
                new Answer(5, "True", 2, false),
                new Answer(6, "False", 3, true),
                new Answer(7, "True", 3, false),
                new Answer(8, "True", 4, false),
                new Answer(9, "False", 4, true),
                new Answer(11, "Higher load capacity.", 5, false),
                new Answer(12, "Greater range of motion.", 5, true),
                new Answer(13, "Safer for newbies.", 5, false),
                new Answer(14, "Easier for beginners.", 5, false),
        };
    }
}
