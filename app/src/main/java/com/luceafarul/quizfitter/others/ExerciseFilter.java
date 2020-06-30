package com.luceafarul.quizfitter.others;


import android.widget.Filter;

import com.luceafarul.quizfitter.adapters.ExercisesAdapter;
import com.luceafarul.quizfitter.model.Exercise;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ExerciseFilter extends Filter {
    private final ExercisesAdapter adapter;

    private final List<Exercise> originalList;

    private final List<Exercise> filteredList;

    public ExerciseFilter(ExercisesAdapter adapter, List<Exercise> originalList) {
        super();
        this.adapter = adapter;
        this.originalList = new LinkedList<>(originalList);
        this.filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        if (constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = constraint.toString();

            for (final Exercise exercise : originalList) {
                if (filterPattern.contains("Push") || filterPattern.contains("Pull") || filterPattern.contains("Legs")) {
                    if (filterPattern.toLowerCase().contains(exercise.getDay())) {
                        filteredList.add(exercise);
                    }
                } else if (filterPattern.toLowerCase().contains(exercise.getCategory())) {
                    filteredList.add(exercise);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.getFilteredExerciseList().clear();
        adapter.getFilteredExerciseList().addAll((ArrayList<Exercise>) results.values);
        adapter.notifyDataSetChanged();
    }
}
