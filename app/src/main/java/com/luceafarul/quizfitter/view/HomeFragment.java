package com.luceafarul.quizfitter.view;


import android.os.Bundle;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.others.SharedPrefsFiles;
import com.luceafarul.quizfitter.view.gamification.QuizFragment;
import com.luceafarul.quizfitter.view.tracking.AddBodyDataFragment;

import java.util.Calendar;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ImageView ivPlay;
    private ImageView ivGym;
    private ImageView ivFood;
    private View view;
    private SharedPrefsFiles sharedPrefs;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_home, container, false);
        ivPlay = view.findViewById(R.id.ivPlay);
        ivFood = view.findViewById(R.id.ivFoodWP);
        ivGym = view.findViewById(R.id.ivGymWP);

        sharedPrefs = SharedPrefsFiles.getInstance(getActivity());
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int lastDay = sharedPrefs.getValue("day");

        if (lastDay != currentDay || sharedPrefs.getString("UpdatedTodayData").equals("no")) {
            if (lastDay != currentDay)
                sharedPrefs.saveString("UpdatedTodayData", "no");
            sharedPrefs.saveInteger("day", currentDay);
            notifyUpdateData();
        }

        ivPlay.setOnClickListener(v -> changeFragment(new QuizFragment()));
        ivGym.setOnClickListener(v -> changeFragment(new ExercisesFragment()));
        ivFood.setOnClickListener(v -> changeFragment(new WIPFragment()));

        return view;
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void notifyUpdateData() {
        View contextView = view.findViewById(R.id.clHome);
        Snackbar snackbar = Snackbar.make(contextView, "Update your progress.", Snackbar.LENGTH_LONG)
                .setAction("Go", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeFragment(new AddBodyDataFragment());
                    }
                });
        View sbView = snackbar.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) sbView.getLayoutParams();
        params.gravity = Gravity.TOP;
        sbView.setLayoutParams(params);
        snackbar.show();
    }
}