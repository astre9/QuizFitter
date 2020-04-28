package com.luceafarul.quizfitter.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.UUID;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class Question implements Parcelable {

    @PrimaryKey
    private int id;
    private String text;
    private String category;
    private String imageName;
    private boolean hasImage;

    public Question() {
    }

    public Question(int id, String text, String category, String imageName, boolean hasImage) {
        this.id = id;
        this.text = text;
        this.category = category;
        this.hasImage = hasImage;
        this.imageName = imageName;
    }

    @Ignore
    protected Question(Parcel in) {
        id = in.readInt();
        text = in.readString();
        category = in.readString();
        hasImage = Boolean.getBoolean(in.readString());
        imageName = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(text);
        dest.writeString(category);
        dest.writeString(String.valueOf(hasImage));
        dest.writeString(imageName);
    }

    public static Question[] populateData() {
        return new Question[]{
                new Question(1, "Which are the disadvantages of a full body split?", "general", "full_body_split", true),
                new Question(2, "Sweat is correlated directly to fat-loss.", "general", "sweat_fat_loss", true),
                new Question(3, "It is possible to spot reduce abdominal fat by doing a lot of abdominal exercises.", "general", "spot_reduce", true),
                new Question(4, "You should stretch before you work out.", "general", "stretch", true),
                new Question(5, "What are the advantages of free weight versus machines?", "general", "free_vs_machines", true)
        };
    }

}
