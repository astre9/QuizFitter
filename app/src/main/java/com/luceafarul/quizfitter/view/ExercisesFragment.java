package com.luceafarul.quizfitter.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.models.Exercise;
import com.luceafarul.quizfitter.others.ExercisesAdapter;
import com.luceafarul.quizfitter.repositories.api.JSONRead;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */

//Fragmentul pentru afisare lista de exercitii

public class ExercisesFragment extends Fragment {

    private ListView lvExercises;
    private ExercisesAdapter exerciseAdapter;
    private Spinner spinnerDay;
    private Spinner spinnerCategory;
    private List<Exercise> exercises;
    String[] categories = new String[5];

    public interface OnDataReceiveCallback {
        void onDataReceived();
    }

    public ExercisesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercises, container, false);

        initUI(view);

        initListeners();

        connect();

        return view;
    }

    private void initUI(View view) {
        lvExercises = view.findViewById(R.id.lvExercises);
        spinnerDay = view.findViewById(R.id.spDays);
        spinnerCategory = view.findViewById(R.id.spCategories);
        String[] days = new String[]{"push", "pull", "legs"};

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity().getApplicationContext(), R.layout.spinner_item, days
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerDay.setAdapter(spinnerArrayAdapter);

        getCategories(new OnDataReceiveCallback() {
            public void onDataReceived() {
                ArrayAdapter<String> spinnerCategoryAdapter = new ArrayAdapter<String>(
                        getActivity().getApplicationContext(), R.layout.spinner_item, categories
                );
                spinnerCategoryAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinnerCategory.setAdapter(spinnerCategoryAdapter);
            }
        });

        exerciseAdapter = new ExercisesAdapter(getActivity(), new ArrayList<Exercise>());
        lvExercises.setAdapter(exerciseAdapter);

    }

    private void initListeners() {
        lvExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), exerciseAdapter.getExerciseList().get(position).toString(), Toast.LENGTH_LONG).show();
            }
        });

        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedItem = parent.getItemAtPosition(position).toString();
                if (exercises != null) {
                    List<Exercise> filteredExercises = exercises.stream()
                            .filter(p -> p.getDay().equals(selectedItem))
                            .collect(Collectors.toList());

                    exerciseAdapter.updateList(filteredExercises);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedItem = parent.getItemAtPosition(position).toString();
                if (exercises != null) {
                    List<Exercise> filteredExercises = exercises.stream()
                            .filter(p -> p.getCategory().equals(selectedItem))
                            .collect(Collectors.toList());

                    exerciseAdapter.updateList(filteredExercises);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void connect() {
        JSONRead jsonRead = new JSONRead() {
            @Override
            protected void onPostExecute(String s) {
                exercises = parseExerciseJson(s);
                exerciseAdapter.updateList(exercises);
            }
        };
        jsonRead.execute();
    }

    private void getCategories(OnDataReceiveCallback callback) {
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
