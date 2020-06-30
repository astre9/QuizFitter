package com.luceafarul.quizfitter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.model.Exercise;
import com.luceafarul.quizfitter.others.ExerciseFilter;

import java.util.List;

// Adaptor personalizat exercitii

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<Exercise> exercises;
    private List<Exercise> filteredExerciseList;
    private FragmentManager manager;
    private ExerciseFilter exerciseFilter;

    public ExercisesAdapter(List<Exercise> exercises, FragmentManager manager) {
        this.exercises = exercises;
        this.filteredExerciseList = exercises;
        this.manager = manager;
    }

    @NonNull

    @Override
    public ExercisesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View exercisesView = inflater.inflate(R.layout.item_exercise, parent, false);

        ExercisesAdapter.ViewHolder viewHolder = new ExercisesAdapter.ViewHolder(exercisesView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisesAdapter.ViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);

        TextView tvName = holder.tvName;
        TextView tvSets = holder.tvSets;
        TextView tvReps = holder.tvReps;
        tvName.setText(exercise.getName());
        String sets = exercise.getReps() + " sets";
        String reps = exercise.getReps() + " repetitions";
        tvSets.setText(sets);
        tvReps.setText(reps);
        ImageView ivExercise = holder.ivExercise;
        CardView cardExercise = holder.cardExercise;
        ivExercise.setImageResource(getImageId(context, exercise.getImageName()));
        /*
        TODO Do something with the card on click event
        */
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    @Override
    public Filter getFilter() {
        if (exerciseFilter == null)
            exerciseFilter = new ExerciseFilter(this, exercises);
        return exerciseFilter;
    }

    public List<Exercise> getFilteredExerciseList() {
        return filteredExerciseList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvReps;
        public TextView tvSets;
        public ImageView ivExercise;
        public CardView cardExercise;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvReps = itemView.findViewById(R.id.tvReps);
            tvSets = itemView.findViewById(R.id.tvSets);
            ivExercise = itemView.findViewById(R.id.ivThumbnail);
            cardExercise = itemView.findViewById(R.id.cardExercise);
        }
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

}


