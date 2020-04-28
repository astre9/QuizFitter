package com.luceafarul.quizfitter.others;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.models.Exercise;

import java.util.ArrayList;
import java.util.List;

// Adaptor personalizat exercitii

public class ExercisesAdapter extends BaseAdapter {

    private Context context;
    private List<Exercise> exerciseList;
    private List<Exercise> filteredData = null;
    private LayoutInflater inflater;

    public ExercisesAdapter(Context context, List<Exercise> cities) {
        this.context = context;
        this.exerciseList = cities;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return exerciseList.size();
    }

    @Override
    public Exercise getItem(int i) {
        return exerciseList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = inflater.inflate(R.layout.exercise_item, viewGroup, false);
        TextView exerciseName = rowView.findViewById(R.id.etExerciseName);
        TextView reps = rowView.findViewById(R.id.etReps);
        TextView sets = rowView.findViewById(R.id.etSets);
        TextView category = rowView.findViewById(R.id.etCategory);
        RatingBar difficulty = rowView.findViewById(R.id.rating);
        ImageView image = rowView.findViewById(R.id.ivExercise);

        sets.setText(String.valueOf(exerciseList.get(i).getSets()));
        reps.setText(String.valueOf(exerciseList.get(i).getReps()));
        category.setText(String.valueOf(exerciseList.get(i).getCategory()));
        exerciseName.setText(exerciseList.get(i).getName());
        difficulty.setRating(exerciseList.get(i).getDifficulty());
        image.setImageResource(getImageId(context, exerciseList.get(i).getImageName()));

        return rowView;
    }

    public void updateList(List<Exercise> newExercises) {
        exerciseList = newExercises;
        notifyDataSetChanged();
    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    public List<Exercise> getExerciseList() {
        return exerciseList;
    }

}


