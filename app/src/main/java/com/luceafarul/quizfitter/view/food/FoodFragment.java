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
import com.luceafarul.quizfitter.model.Day;
import com.luceafarul.quizfitter.model.Food;
import com.luceafarul.quizfitter.model.Meal;
import com.luceafarul.quizfitter.repositories.room.DataBase;
import com.luceafarul.quizfitter.repositories.room.operations.meal.UpdateMealAsync;

import java.io.File;
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
    Button btnNext;
    Button btnPrevious;
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
        btnNext = view.findViewById(R.id.btnNext);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        btnImage = view.findViewById(R.id.btnImage);
        ivMeal = view.findViewById(R.id.ivMeal);

        meal = getArguments().getParcelable("meal");

        String type = getArguments().getString("type");

        atvMealName.setInputType(InputType.TYPE_NULL);

        if (meal != null) {
            if (meal.getImageName() != null) {
                setImageBitmap();
            }

            final Handler handler = new Handler();
            (new Thread(new Runnable() {
                @Override
                public void run() {
                    DataBase dataBase = DataBase.getInstance(getActivity());

                    foodList = dataBase.foodsDAO().getAllByMeal(meal.getId());
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

        checkNavigationButtons();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFoodIndex++;
                checkNavigationButtons();
                goToNewFoodCard(meal.getId());
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFoodIndex--;
                checkNavigationButtons();
                goToNewFoodCard(meal.getId());
            }
        });

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
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
        ivMeal.setBackground(bitmapDrawable);
        ivMeal.setRotation(90);
        ivMeal.setScaleType(ImageView.ScaleType.CENTER);
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

    private void checkNavigationButtons() {
        btnPrevious.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.VISIBLE);
        if (foodList != null) {
            if (selectedFoodIndex == foodList.size()) {
                btnNext.setVisibility(View.GONE);
            }
            if (selectedFoodIndex == 0) {
                btnPrevious.setVisibility(View.GONE);
            }
        } else {
            btnPrevious.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
        }

    }

    private void goToNewFoodCard(int mealId) {
        FoodCardFragment foodCardFragment = new FoodCardFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("food", foodList.get(selectedFoodIndex));
        bundle.putInt("mealId", mealId);
        foodCardFragment.setArguments(bundle);
        changeFragment(foodCardFragment, R.id.cardFragmentContainer);
    }

    private void initTypeCases(Day day, Meal meal, String type, View view) {

        if (type.equals("add")) {
            tilName.setVisibility(View.VISIBLE);
            btnSaveMeal.setVisibility(View.VISIBLE);
            btnImage.setVisibility(View.GONE);
            tvDate.setText(sdf.format(new Date()));
            String[] mealNames = {"Breakfast", "Brunch", "Preworkout", "Lunch", "Snack", "Dinner"};
            ArrayAdapter<String> mealNameAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mealNames);
            atvMealName.setAdapter(mealNameAdapter);

            LinearLayout llNavigation = view.findViewById(R.id.llNavigation);
            llNavigation.setVisibility(View.GONE);
            btnSaveMeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                DataBase dataBase = DataBase.getInstance(getActivity());

                                Day day = new Day(tvDate.getText().toString(), FirebaseAuth.getInstance().getUid());
                                day.setComment("This is one hell of a hard diet...");
                                int newDayId = (int) dataBase.daysDao().insert(day);

                                Meal meal = new Meal(atvMealName.getText().toString(), newDayId);
                                int newMealId = (int) dataBase.mealsDao().insertMeal(meal);

                                Fragment foodCardFragment = new FoodCardFragment();
                                Bundle bundle = new Bundle();
                                bundle.putInt("mealId", newMealId);
                                foodCardFragment.setArguments(bundle);
                                changeFragment(foodCardFragment, R.id.cardFragmentContainer);
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
            FoodCardFragment foodCardFragment = new FoodCardFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("food", foodList.get(selectedFoodIndex));
            bundle.putInt("mealId", meal.getId());
            foodCardFragment.setArguments(bundle);
            changeFragment(foodCardFragment, R.id.cardFragmentContainer);
        }
        if (day != null && meal != null) {
            tvName.setText(meal.getName());
            tvDate.setText(day.getDate());
        }
    }

    private void changeFragment(Fragment fragment, int containerId) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
