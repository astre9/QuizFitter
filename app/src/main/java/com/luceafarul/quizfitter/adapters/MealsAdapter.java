package com.luceafarul.quizfitter.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.model.Meal;
import com.luceafarul.quizfitter.view.food.FoodFragment;

import java.io.File;
import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.ViewHolder> {

    private List<Meal> meals;
    private Context context;
    private FragmentManager manager;

    public MealsAdapter(List<Meal> meals, FragmentManager manager) {
        this.meals = meals;
        this.manager = manager;
    }


    public void setList(List<Meal> meals) {
        this.meals = meals;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_meal, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MealsAdapter.ViewHolder holder, int position) {
        Meal meal = meals.get(position);

        TextView tvMealName = holder.tvMealName;
        tvMealName.setText(meal.getName());
        ImageView ivMeal = holder.ivMeal;
        CardView cardMeal = holder.cardMeal;

        cardMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "edit_meal");
                bundle.putParcelable("meal", meal);
                Fragment foodFragment = new FoodFragment();
                foodFragment.setArguments(bundle);
                changeFragment(foodFragment);
            }
        });

        if (meal.getImageName() != null) {
            ivMeal.setBackground(getImageMeal(meal.getImageName()));
            ivMeal.setRotation(90);
            ivMeal.setScaleType(ImageView.ScaleType.CENTER);
        }
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvMealName;
        public ImageView ivMeal;
        public CardView cardMeal;

        public ViewHolder(View itemView) {
            super(itemView);

            tvMealName = itemView.findViewById(R.id.tvMealName);
            ivMeal = itemView.findViewById(R.id.ivMeal);
            cardMeal = itemView.findViewById(R.id.cardMeal);
        }
    }

    private BitmapDrawable getImageMeal(String imageName) {
        String filePath = Environment.getExternalStorageDirectory() + "/QFMeals/" + imageName;
        BitmapDrawable bitmapDrawable;
        if (!new File(filePath).exists()) {
            final int imageId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageId);
            bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            bitmapDrawable = new BitmapDrawable(bitmap);
        }

        return bitmapDrawable;
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
