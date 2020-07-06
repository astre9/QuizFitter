package com.luceafarul.quizfitter.view.food;


import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.adapters.FoodAdapter;
import com.luceafarul.quizfitter.adapters.FoodsAdapter;
import com.luceafarul.quizfitter.model.Day;
import com.luceafarul.quizfitter.model.Food;
import com.luceafarul.quizfitter.model.Meal;
import com.luceafarul.quizfitter.repositories.room.DataBase;
import com.luceafarul.quizfitter.repositories.room.operations.meal.UpdateMealAsync;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodFragment extends Fragment {

    private static final int CAMERA_REQUEST = 1888;

    TextView tvName;
    TextView tvDate;
    Button btnSaveMeal;
    FloatingActionButton btnImage;
    ImageView ivMeal;
    TextInputLayout tilName;
    List<Food> foodList;
    int selectedFoodIndex = 0;
    Day day;
    Meal meal;
    AutoCompleteTextView atvMealName;

    static final DateFormat sdf = new SimpleDateFormat("EEE, d MMMM yyyy");

    public FoodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        tvName = view.findViewById(R.id.tvName);
        tvDate = view.findViewById(R.id.tvDate);
        tilName = view.findViewById(R.id.tilName);
        atvMealName = view.findViewById(R.id.atvMealName);
        btnSaveMeal = view.findViewById(R.id.btnSaveMeal);
        btnImage = view.findViewById(R.id.btnImage);
        ivMeal = view.findViewById(R.id.ivMeal);

        meal = getArguments().getParcelable("meal");

        String type = getArguments().getString("type");

        atvMealName.setInputType(InputType.TYPE_NULL);

        if (meal != null) {
            if (meal.getImageName() != null) {
                setImageBitmap();
            } else {
                ivMeal.setBackground(getActivity().getDrawable(R.drawable.not_found_food));
            }

            final Handler handler = new Handler();
            (new Thread(new Runnable() {
                @Override
                public void run() {
                    DataBase dataBase = DataBase.getInstance(getActivity());

                    foodList = dataBase.foodsDAO().getAllByMeal(meal.getId());
                    foodList.add(new Food());
                    day = dataBase.daysDao().getById(meal.getDayId());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            initTypeCases(day, meal, type, view);
                        }
                    });
                }
            })).start();
        } else {
            initTypeCases(day, meal, type, view);
        }

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "QFMeals");
                imagesFolder.mkdirs();
                String imageName = "food" + meal.getId() + ".jpg";

                updateMeal(imageName);
                meal.setImageName(imageName);
                File image = new File(imagesFolder, imageName);
                Uri uriSavedImage = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", image);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        return view;
    }

    private void setImageBitmap() {
        String filePath = Environment.getExternalStorageDirectory() + "/QFMeals/" + meal.getImageName();
        BitmapDrawable bitmapDrawable;
        Bitmap bitmap;

        if (!new File(filePath).exists()) {
            final int imageId = getResources().getIdentifier(meal.getImageName(), "drawable", getActivity().getPackageName());
            bitmap = BitmapFactory.decodeResource(getResources(), imageId);
//            bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        } else {
            bitmap = BitmapFactory.decodeFile(filePath);
//            bitmapDrawable = new BitmapDrawable(bitmap);
        }
        ivMeal.setImageBitmap(bitmap);
//        ivMeal.setBackground(bitmapDrawable);
    }

    @SuppressLint("StaticFieldLeak")
    private void updateMeal(String imageName) {
        meal.setImageName(imageName);
        new UpdateMealAsync(getActivity()) {
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute(meal);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                setImageBitmap();
            }
        }
    }

    private void initTypeCases(Day day, Meal meal, String type, View view) {
        if (type.equals("add") || type.equals("add_meal")) {
            tilName.setVisibility(View.VISIBLE);
            btnSaveMeal.setVisibility(View.VISIBLE);
            btnImage.setVisibility(View.GONE);
            String mealDate = sdf.format(new Date());
            if (type.equals("add_meal")) {
                mealDate = getArguments().getString("date");
            }
            tvDate.setText(mealDate);

            String[] mealNames = {"Breakfast", "Brunch", "Preworkout", "Lunch", "Snack", "Dinner"};
            ArrayAdapter<String> mealNameAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mealNames);
            atvMealName.setAdapter(mealNameAdapter);

            btnSaveMeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                DataBase dataBase = DataBase.getInstance(getActivity());

                                Day day = new Day(tvDate.getText().toString(), FirebaseAuth.getInstance().getUid());
//                                day.setComment("This is one hell of a hard diet...");
                                int dayId;
                                if (type.equals("add_meal")) {
                                    dayId = dataBase.daysDao().getByDate(getArguments().getString("date")).getId();
                                } else {
                                    dayId = (int) dataBase.daysDao().insert(day);
                                }

                                Meal meal = new Meal(atvMealName.getText().toString(), dayId);
                                int newMealId = (int) dataBase.mealsDao().insertMeal(meal);
                                tilName.setVisibility(View.GONE);
                                btnSaveMeal.setVisibility(View.GONE);
                                showFoodsList(view, newMealId);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
        } else if (type.equals("edit_meal")) {
            tvName.setText(meal.getName());
            tvDate.setText(day.getDate());
            showFoodsList(view, meal.getId());
        }
        if (day != null && meal != null) {
            tvName.setText(meal.getName());
            tvDate.setText(day.getDate());
        }
    }

    private void showFoodsList(View view, int mealId) {
        FoodsAdapter foodAdapter = new FoodsAdapter(foodList, getActivity().getSupportFragmentManager(), mealId);
        RecyclerView rvFoods = view.findViewById(R.id.rvFoods);
        LinearLayoutManager layoutManagerHorizontal = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvFoods.setLayoutManager(layoutManagerHorizontal);
        rvFoods.setVisibility(View.VISIBLE);
        rvFoods.setAdapter(foodAdapter);
    }
}
