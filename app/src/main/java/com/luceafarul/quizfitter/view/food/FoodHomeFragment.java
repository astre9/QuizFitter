package com.luceafarul.quizfitter.view.food;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.adapters.DaysAdapter;
import com.luceafarul.quizfitter.adapters.MealsAdapter;
import com.luceafarul.quizfitter.interfaces.OnDaySelectedCallback;
import com.luceafarul.quizfitter.model.Day;
import com.luceafarul.quizfitter.model.Meal;
import com.luceafarul.quizfitter.repositories.room.operations.day.GetDaysAsync;
import com.luceafarul.quizfitter.repositories.room.operations.day.GetLastDayAsync;
import com.luceafarul.quizfitter.repositories.room.operations.meal.GetMealsAsync;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodHomeFragment extends Fragment {

    List<Meal> allMeals;
    List<Day> allDays;
    Day selectedDay;
    OnDaySelectedCallback callback;

    RecyclerView rvMeals;
    RecyclerView rvDays;
    EditText etDate;

    DaysAdapter daysAdapter;
    MealsAdapter mealsAdapter;

    static final DateFormat sdf = new SimpleDateFormat("EEE, d MMMM yyyy");

    public FoodHomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_home, container, false);

        callback = new OnDaySelectedCallback() {
            @Override
            public void onDaySelected(String date) {
                etDate.setText(date);
                setSelectedDayByDate(date);
                mealsAdapter.setList(getSelectedDaysMeals());
            }
        };

        daysAdapter = new DaysAdapter(allDays, callback);

        initViews(view);

        getToday();
        initDays();
        initMeals();

//        final FoodListViewModel viewModel = ViewModelProviders.of(this).get(FoodListViewModel.class);
//        observeViewModel(viewModel);

        Button btnAdd = view.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "add");
                Fragment foodFragment = new FoodFragment();
                foodFragment.setArguments(bundle);
                changeFragment(foodFragment);
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog;

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                Date date = null;
                                try {
                                    date = format.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    etDate.setText(sdf.format(date));
                                    setSelectedDayByDate(etDate.getText().toString());
                                    mealsAdapter.setList(getSelectedDaysMeals());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        return view;
    }

    private void setSelectedDayByDate(String date) {
        Optional<Day> wantedDay = allDays.stream()
                .filter(d -> d.getDate().equals(date))
                .findFirst();
        if (wantedDay.isPresent()) {
            selectedDay = wantedDay.get();
        } else {
            selectedDay = null;
        }
    }

    private void initViews(View view) {
        etDate = view.findViewById(R.id.etDate);
        rvMeals = view.findViewById(R.id.rvMeals);
        rvDays = view.findViewById(R.id.rvDays);

        LinearLayoutManager layoutManagerHorizontal = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvMeals.setLayoutManager(layoutManagerHorizontal);
        LinearLayoutManager layoutManagerVertical = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvDays.setLayoutManager(layoutManagerVertical);

    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @SuppressLint("StaticFieldLeak")
    private void initDays() {
        new GetDaysAsync(getActivity()) {
            @Override
            protected void onPostExecute(List<Day> days) {
                super.onPostExecute(days);

                allDays = days;
                daysAdapter = new DaysAdapter(days, callback);
                rvDays.setAdapter(daysAdapter);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void initMeals() {
        new GetMealsAsync(getActivity()) {
            @Override
            protected void onPostExecute(List<Meal> meals) {
                super.onPostExecute(meals);
                allMeals = meals;
                List<Meal> todaysMeals = getSelectedDaysMeals();
                mealsAdapter = new MealsAdapter(todaysMeals, getActivity().getSupportFragmentManager());
                rvMeals.setAdapter(mealsAdapter);
            }
        }.execute();
    }

    private List<Meal> getSelectedDaysMeals() {
        if (selectedDay != null) {
            List<Meal> filteredMeals = allMeals.stream()
                    .filter(m -> m.getDayId() == selectedDay.getId())
                    .collect(Collectors.toList());
            return filteredMeals;
        } else {
            return new ArrayList<>();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void getToday() {
        new GetLastDayAsync(getActivity()) {
            @Override
            protected void onPostExecute(Day day) {
                super.onPostExecute(day);
                etDate.setText(day.getDate());
                selectedDay = day;
            }
        }.execute();
    }
}
