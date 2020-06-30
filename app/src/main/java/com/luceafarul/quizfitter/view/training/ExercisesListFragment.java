package com.luceafarul.quizfitter.view.training;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.adapters.ExercisesAdapter;
import com.luceafarul.quizfitter.model.Exercise;
import com.luceafarul.quizfitter.repositories.api.JSONRead;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExercisesListFragment extends Fragment {

    List<Exercise> exercises;
    RecyclerView rvExercises;
    String[] categories = new String[5];
    ChipGroup chipsDays;
    ChipGroup chipsCategory;
    ExercisesAdapter adapter;

    public ExercisesListFragment() {
        // Required empty public constructor
    }

    public interface OnDataReceiveCallback {
        void onDataReceived();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercises_list, container, false);

        initViews(view);

        getExercises();

        chipsDays.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                List<Integer> selectedDayIds = chipsDays.getCheckedChipIds();
                String filterText = "";
                for (Integer id : selectedDayIds) {
                    Chip selectedChip = view.findViewById(id);
                    filterText += " " + selectedChip.getText();
                }
                adapter.getFilter().filter(filterText);
            }
        });

        chipsCategory.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                List<Integer> selectedCategoryIds = chipsCategory.getCheckedChipIds();
                String filterText = "";
                for (Integer id : selectedCategoryIds) {
                    Chip selectedChip = group.findViewById(id);
                    filterText += " " + selectedChip.getText();
                }
                adapter.getFilter().filter(filterText);
            }
        });

        return view;
    }

    private void initViews(View view) {
        rvExercises = view.findViewById(R.id.rvExercises);
        chipsDays = view.findViewById(R.id.chipsDays);
        chipsCategory = view.findViewById(R.id.chipsCategory);
    }

    @SuppressLint("StaticFieldLeak")
    private void getExercises() {
        JSONRead jsonRead = new JSONRead() {
            @Override
            protected void onPostExecute(String s) {
                exercises = parseExerciseJson(s);
                adapter = new ExercisesAdapter(exercises, getActivity().getSupportFragmentManager());
                rvExercises.setAdapter(adapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rvExercises.setLayoutManager(layoutManager);
            }
        };
        jsonRead.execute();
    }

    private void getCategories(ExercisesListFragment.OnDataReceiveCallback callback) {
        DatabaseReference categoryReference = FirebaseDatabase.getInstance().getReference("exerciseCategories");

        ValueEventListener categoryListener = new ValueEventListener() {
            int i = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    categories[i++] = dsp.child("name").getValue().toString();
                }
                callback.onDataReceived();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadExerciseCategory:onCancelled", databaseError.toException());
            }
        };
        categoryReference.addListenerForSingleValueEvent(categoryListener);
    }
}
