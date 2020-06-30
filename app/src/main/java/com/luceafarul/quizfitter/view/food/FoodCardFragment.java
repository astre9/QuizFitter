package com.luceafarul.quizfitter.view.food;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.model.Food;
import com.luceafarul.quizfitter.repositories.room.operations.food.DeleteFoodAsync;
import com.luceafarul.quizfitter.repositories.room.operations.food.InsertFoodAsync;
import com.luceafarul.quizfitter.repositories.room.operations.food.UpdateFoodAsync;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodCardFragment extends Fragment {

    EditText etName;
    EditText etCalories;
    EditText etProtein;
    EditText etCarbs;
    EditText etFat;
    EditText etQuantity;
    Button btnSave;
    Button btnDelete;
    Food food;
    int mealId;

    public FoodCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_card, container, false);

        food = getArguments().getParcelable("food");
        mealId = getArguments().getInt("mealId");

        initViews(view);

        if (food != null) {
            etName.setText(food.getName());
            etCalories.setText(String.valueOf(food.getCalories()));
            etCarbs.setText(String.valueOf(food.getCarbohydrate()));
            etProtein.setText(String.valueOf(food.getProtein()));
            etFat.setText(String.valueOf(food.getFat()));
            etQuantity.setText(String.valueOf(food.getQuantity()));
            btnDelete.setVisibility(View.VISIBLE);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateFood();
                }
            });
        } else {
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFood();
                }
            });
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFood();
            }
        });

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteFood() {
        new DeleteFoodAsync(getActivity()) {
            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(getActivity(), "Food deleted!", Toast.LENGTH_SHORT).show();
            }
        }.execute(food);
    }

    @SuppressLint("StaticFieldLeak")
    private void updateFood() {
        food.setName(etName.getText().toString());
        food.setCalories(Integer.parseInt(etCalories.getText().toString()));
        food.setProtein(Integer.parseInt(etProtein.getText().toString()));
        food.setCarbohydrate(Integer.parseInt(etCarbs.getText().toString()));
        food.setFat(Integer.parseInt(etFat.getText().toString()));
        food.setQuantity(Integer.parseInt(etQuantity.getText().toString()));
        new UpdateFoodAsync(getActivity()) {
            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(getActivity(), "Food updated!", Toast.LENGTH_SHORT).show();
            }
        }.execute(food);
    }

    @SuppressLint("StaticFieldLeak")
    private void addFood() {
        Food food = new Food(etName.getText().toString(), Integer.parseInt(etQuantity.getText().toString()), Integer.parseInt(etCalories.getText().toString()), mealId);
        food.setProtein(Integer.parseInt(etProtein.getText().toString()));
        food.setCarbohydrate(Integer.parseInt(etCarbs.getText().toString()));
        food.setFat(Integer.parseInt(etFat.getText().toString()));
        new InsertFoodAsync(getActivity()) {
            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(getActivity(), "Food added!", Toast.LENGTH_SHORT).show();
            }
        }.execute(food);
    }

    private void initViews(View view) {
        etName = view.findViewById(R.id.etName);
        etProtein = view.findViewById(R.id.etProtein);
        etCalories = view.findViewById(R.id.etCalories);
        etCarbs = view.findViewById(R.id.etCarbs);
        etFat = view.findViewById(R.id.etFat);
        etQuantity = view.findViewById(R.id.etQuantity);
        btnSave = view.findViewById(R.id.btnSave);
        btnDelete = view.findViewById(R.id.btnDelete);
    }
}
