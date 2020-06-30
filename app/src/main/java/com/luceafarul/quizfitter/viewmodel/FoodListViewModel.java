package com.luceafarul.quizfitter.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.luceafarul.quizfitter.model.Day;
import com.luceafarul.quizfitter.model.Meal;
import com.luceafarul.quizfitter.repositories.room.operations.day.DayRepository;

import java.util.List;

public class FoodListViewModel extends AndroidViewModel {
//    private final LiveData<List<Meal>> mealListObservable;
//    private final LiveData<List<Day>> dayListObservable;

    public FoodListViewModel(Application application) {
        super(application);

//        dayListObservable = DayRepository.getInstance(application).getAll();
    }

//    public LiveData<List<Day>> getDayListObservable() {
//        return dayListObservable;
//    }
}
