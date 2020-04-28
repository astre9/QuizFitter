package com.luceafarul.quizfitter.view.classification.tabfragments;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.models.Exercise;
import com.luceafarul.quizfitter.repositories.api.JSONRead;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultsTabFragment extends Fragment {

    private String imageName;
    private TextView tvExerciseName;
    private TextView tcExerciseCategory;
    private TextView tcExerciseDetails;
    private ImageView ivExercise;

    public ResultsTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_results_tab, container, false);

        imageName = getArguments().getString("imageName");

        tvExerciseName = view.findViewById(R.id.tvExerciseName);
        tcExerciseCategory = view.findViewById(R.id.tcExerciseCategory);
        tcExerciseDetails = view.findViewById(R.id.tcExerciseDetails);
        ivExercise = view.findViewById(R.id.avatar_image);

        Resources resources = getActivity().getResources();
        int resourceId = resources.getIdentifier(imageName, "drawable",
                getActivity().getPackageName());
        ivExercise.setImageResource(resourceId);

        getExerciseData();

        return view;
    }

    private void getExerciseData() {
        JSONRead jsonRead = new JSONRead() {
            @Override
            protected void onPostExecute(String s) {
                List<Exercise> exercises = parseExerciseJson(s);
                for (Exercise exercise : exercises) {
                    if (exercise.getImageName().equals(imageName)) {
                        tvExerciseName.setText(exercise.getName());
                        tcExerciseCategory.setText(exercise.getCategory());
                        tcExerciseDetails.setText(exercise.getDetails());
                    }
                }
            }
        };
        jsonRead.execute();
    }
}
