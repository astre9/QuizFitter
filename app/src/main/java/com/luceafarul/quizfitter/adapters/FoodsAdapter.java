package com.luceafarul.quizfitter.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.model.Food;
import com.luceafarul.quizfitter.model.Meal;
import com.luceafarul.quizfitter.repositories.room.operations.food.DeleteFoodAsync;
import com.luceafarul.quizfitter.repositories.room.operations.food.InsertFoodAsync;
import com.luceafarul.quizfitter.repositories.room.operations.food.UpdateFoodAsync;
import com.luceafarul.quizfitter.view.food.FoodFragment;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.ViewHolder> {

    private List<Food> foods;
    private Context context;
    private FragmentManager manager;
    int mealId;

    public FoodsAdapter(List<Food> foods, FragmentManager manager, int mealId) {
        this.foods = foods;
        this.manager = manager;
        this.mealId = mealId;
    }


    public void setList(List<Food> foods) {
        this.foods = foods;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View foodView = inflater.inflate(R.layout.fragment_food_card, parent, false);

        ViewHolder viewHolder = new ViewHolder(foodView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodsAdapter.ViewHolder holder, int position) {
        Food food = foods.get(position);

        EditText etName = holder.etName;
        EditText etProtein = holder.etProtein;
        EditText etCalories = holder.etCalories;
        EditText etCarbs = holder.etCarbs;
        EditText etFat = holder.etFat;
        EditText etQuantity = holder.etQuantity;
        Button btnSave = holder.btnSave;
        Button btnDelete = holder.btnDelete;

        if (position < foods.size() - 1) {
            etName.setText(food.getName());
            etProtein.setText(String.valueOf(food.getProtein()));
            etCarbs.setText(String.valueOf(food.getCarbohydrate()));
            etFat.setText(String.valueOf(food.getFat()));
            etCalories.setText(String.valueOf(food.getCalories()));
            etQuantity.setText(String.valueOf(food.getQuantity()));
            btnSave.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onClick(View v) {
                    food.setName(etName.getText().toString());
                    food.setCalories(Integer.parseInt(etCalories.getText().toString()));
                    food.setProtein(Integer.parseInt(etProtein.getText().toString()));
                    food.setCarbohydrate(Integer.parseInt(etCarbs.getText().toString()));
                    food.setFat(Integer.parseInt(etFat.getText().toString()));
                    food.setQuantity(Integer.parseInt(etQuantity.getText().toString()));
                    new UpdateFoodAsync(context) {
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            Toast.makeText(context, "Food updated!", Toast.LENGTH_SHORT).show();
                        }
                    }.execute(food);
                }
            });
        } else {
            btnSave.setText("ADD");
            btnSave.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onClick(View v) {
                    Food food = new Food(etName.getText().toString(), Integer.parseInt(etQuantity.getText().toString()), Integer.parseInt(etCalories.getText().toString()), mealId);
                    food.setProtein(Integer.parseInt(etProtein.getText().toString()));
                    food.setCarbohydrate(Integer.parseInt(etCarbs.getText().toString()));
                    food.setFat(Integer.parseInt(etFat.getText().toString()));
                    new InsertFoodAsync(context) {
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            Toast.makeText(context, "Food added!", Toast.LENGTH_SHORT).show();
                        }
                    }.execute(food);
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onClick(View v) {
                    new DeleteFoodAsync(context) {
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            Toast.makeText(context, "Food deleted!", Toast.LENGTH_SHORT).show();
                        }
                    }.execute(food);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public EditText etName;
        public EditText etCalories;
        public EditText etProtein;
        public EditText etCarbs;
        public EditText etFat;
        public EditText etQuantity;
        public Button btnSave;
        public Button btnDelete;

        public ViewHolder(View view) {
            super(view);

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
}
