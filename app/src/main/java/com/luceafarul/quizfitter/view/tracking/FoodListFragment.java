package com.luceafarul.quizfitter.view.tracking;


import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.model.Food;
import com.luceafarul.quizfitter.adapters.FoodAdapter;
import com.luceafarul.quizfitter.others.SharedPrefsFiles;
import com.luceafarul.quizfitter.repositories.room.operations.food.GetFoodAsync;
import com.luceafarul.quizfitter.view.food.FoodFragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodListFragment extends Fragment {

    private ListView lvFood;
    private FoodAdapter foodAdapter;
    private Spinner spinner;
    private List<Food> foodList;
    FloatingActionButton fab;

    public FoodListFragment() {
        // Required empty public constructor
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);
        lvFood = view.findViewById(R.id.lvFood);
        fab = view.findViewById(R.id.fab);
        SharedPrefsFiles sharedPrefs = SharedPrefsFiles.getInstance(getContext());

        String loggedUser = FirebaseAuth.getInstance().getUid();

        new GetFoodAsync(getContext()) {
            @Override
            protected void onPostExecute(List<Food> foods) {
                super.onPostExecute(foods);
                foodAdapter = new FoodAdapter(getContext(), foods);
                lvFood.setAdapter(foodAdapter);
                lvFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        int selectedFoodId = foodAdapter.getFoodList().get(position).fid;
//                        sharedPrefs.saveString("selectedFoodId", String.valueOf(selectedFoodId));
//                        changeFragment(new FoodFragment());
                    }
                });
            }
        }.execute(loggedUser);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new FoodFragment());
            }
        });
        return view;
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
