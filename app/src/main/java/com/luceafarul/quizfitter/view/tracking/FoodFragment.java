package com.luceafarul.quizfitter.view.tracking;


import android.annotation.SuppressLint;

import androidx.fragment.app.FragmentTransaction;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.models.Food;
import com.luceafarul.quizfitter.models.RoomUser;
import com.luceafarul.quizfitter.others.SharedPrefsFiles;
import com.luceafarul.quizfitter.repositories.room.DataBase;
import com.luceafarul.quizfitter.repositories.room.operations.GetFoodAsync;
import com.luceafarul.quizfitter.repositories.room.operations.InsertFoodAsync;
import com.luceafarul.quizfitter.repositories.room.operations.UpdateFoodAsync;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodFragment extends Fragment {

    EditText etName;
    EditText etProtein;
    EditText etCarb;
    EditText etCalories;
    EditText etFat;
    Button btnAdd;
    Button btnTest;
    Button btnChart;

    SharedPrefsFiles prefs;
    DataBase dataBase;

    String fid;
    Food selectedFood = null;

    static final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public FoodFragment() {
        // Required empty public constructor
    }


    @SuppressLint({"SetTextI18n", "StaticFieldLeak"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        etProtein = view.findViewById(R.id.etProtein);
        etCarb = view.findViewById(R.id.etCarb);
        etFat = view.findViewById(R.id.etFat);
        etName = view.findViewById(R.id.etFoodName);
        etCalories = view.findViewById(R.id.etCalories);
        btnAdd = view.findViewById(R.id.btnAddFood);
        btnTest = view.findViewById(R.id.btnTest);
        btnChart = view.findViewById(R.id.btnChart);

        dataBase = DataBase.getInstance(getActivity());
        prefs = SharedPrefsFiles.getInstance(getActivity());

        RoomUser user = new RoomUser();
        try {
            user.uid = Integer.parseInt(prefs.getString("id"));
            fid = prefs.getString("selectedFoodId");
            new GetFoodAsync(getContext()) {
                @Override
                protected void onPostExecute(List<Food> foods) {
                    super.onPostExecute(foods);
                    if (foods.size() > 0 && !fid.equals(""))
                        selectedFood = foods.stream()
                                .filter(food -> Integer.parseInt(fid) == food.fid)
                                .findAny()
                                .orElse(null);
                    if (selectedFood != null) {
                        updateUI(selectedFood);
                    }
                }
            }.execute(user);
        } catch (Exception e) {
            Log.e("eroare", e.getMessage());
        }

        if (!fid.equals("")) {
            btnAdd.setText("Update");
        }
        if (fid.equals("")) {
            btnChart.setVisibility(View.GONE);
        } else {
            btnChart.setVisibility(View.VISIBLE);
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food food = new Food();
                food.userId = Integer.parseInt(prefs.getString("id"));
                food.fid = new Random().nextInt();
                food.name = etName.getText().toString();
                food.protein = Integer.parseInt(etProtein.getText().toString());
                food.carb = Integer.parseInt(etCarb.getText().toString());
                food.fat = Integer.parseInt(etFat.getText().toString());
                food.calories = Integer.parseInt(etCalories.getText().toString());
                Date date = new Date();
//                food.parsedDate = parsedDate;
                if (fid.equals("")) {
                    saveFood(food);
                } else {
                    food.fid = Integer.parseInt(fid);
                    updateFood(food);
                }
                Toast.makeText(getContext(), "Food added to today", Toast.LENGTH_LONG).show();
            }
        });
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new FoodListFragment());
//                try {
//                    exportToCSV();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name", etName.getText().toString());
                bundle.putInt("protein", Integer.parseInt(etProtein.getText().toString()));
                bundle.putInt("fat", Integer.parseInt(etFat.getText().toString()));
                bundle.putInt("carb", Integer.parseInt(etCarb.getText().toString()));
                Fragment frg = new ChartFragment();
                frg.setArguments(bundle);
                changeFragment(frg);
            }
        });
        return view;
    }

    private void saveFood(Food food) {
        new InsertFoodAsync(getActivity()).execute(food);
    }

    private void updateFood(Food food) {
        new UpdateFoodAsync(getActivity()).execute(food);
    }

    private void exportToCSV() throws IOException {
        String baseDir = getContext().getFilesDir().getAbsolutePath();
        Date date = new Date();
        String fileName = sdf.format(date) + "_food" + ".csv";
        String filePath = baseDir + File.separator + fileName;
        File f = new File(filePath);
        CSVWriter writer;
        FileWriter fileWriter;
        // File exist
        if (f.exists() && !f.isDirectory()) {
            fileWriter = new FileWriter(filePath, true);
            writer = new CSVWriter(fileWriter);
        } else {
            writer = new CSVWriter(new FileWriter(filePath));
        }

        String[] data = {"Ship Name", "Scientist Name", "..."};

        writer.writeNext(data);

        writer.close();
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void updateUI(Food selectedFood) {
        etCalories.setText(String.valueOf(selectedFood.calories));
        etCarb.setText(String.valueOf(selectedFood.carb));
        etFat.setText(String.valueOf(selectedFood.fat));
        etProtein.setText(String.valueOf(selectedFood.protein));
        etName.setText(selectedFood.name);
    }
}
