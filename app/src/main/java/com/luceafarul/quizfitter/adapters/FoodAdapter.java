package com.luceafarul.quizfitter.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.model.Food;

import java.util.List;

public class FoodAdapter extends BaseAdapter {
    private Context context;
    private List<Food> foodList;
    private List<Food> filteredData = null;
    private LayoutInflater inflater;

    public FoodAdapter(Context context, List<Food> food) {
        this.context = context;
        this.foodList = food;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Food getItem(int i) {
        return foodList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.food_item, viewGroup, false);
        TextView tvFoodName = rowView.findViewById(R.id.etFoodName);
        TextView tvQuantity = rowView.findViewById(R.id.etQuantity);
        TextView tvProtein = rowView.findViewById(R.id.etProtein);
        TextView tvFat = rowView.findViewById(R.id.etFat);
        TextView tvCarb = rowView.findViewById(R.id.etCarbs);
        TextView tvCalories = rowView.findViewById(R.id.etCalories);

//        tvFoodName.setText(String.valueOf(foodList.get(i).name));
//        tvCalories.setText(String.valueOf(foodList.get(i).calories));
//        tvQuantity.setText(String.valueOf(foodList.get(i).protein));
//        tvProtein.setText(String.valueOf(foodList.get(i).protein));
//        tvCarb.setText(String.valueOf(foodList.get(i).carbohydrate));
//        tvFat.setText(String.valueOf(foodList.get(i).fat));

//        btnTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new DeleteFoodAsync(context).execute(foodList.get(i));
//            }
//        });

        return rowView;
    }

    public void updateList(List<Food> newFood) {
        foodList = newFood;
        notifyDataSetChanged();
    }

    public List<Food> getFoodList() {
        return foodList;
    }
}
