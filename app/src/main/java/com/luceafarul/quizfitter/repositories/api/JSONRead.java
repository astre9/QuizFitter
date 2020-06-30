package com.luceafarul.quizfitter.repositories.api;

import android.os.AsyncTask;
import android.util.Log;

import com.luceafarul.quizfitter.model.Exercise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// Clasa pentru obtinere date din retea prin request OKHttp3 la url

public class JSONRead extends AsyncTask<Void, Void, String> {

    final OkHttpClient client = new OkHttpClient();

    final Request request = new Request.Builder()
            .url("http://www.mocky.io/v2/5e9eff2d2d00005100cb7902")
            .build();

    @Override
    protected String doInBackground(Void... params) {
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return null;
            }
            return response.body().string();

        } catch (IOException e) {
            Log.e("jsonread error", e.getMessage());
        }
        return null;
    }

    // Metoda de parsare json returnat de request

    protected List<Exercise> parseExerciseJson(String json) {
        List<Exercise> exercises = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonPush = jsonObject.getJSONObject("push");
            JSONObject jsonPull = jsonObject.getJSONObject("pull");
            JSONObject jsonLegs = jsonObject.getJSONObject("legs");

            JSONArray arrayPush = jsonPush.getJSONArray("exercises");
            JSONArray arrayPull = jsonPull.getJSONArray("exercises");
            JSONArray arrayLegs = jsonLegs.getJSONArray("exercises");

            for (int i = 0; i < arrayPush.length(); i++) {
                JSONObject exerciseObject = arrayPush.getJSONObject(i);
                Exercise exercise = new Exercise();
                exercise.setId(exerciseObject.getString("id"));
                exercise.setDay("push");
                exercise.setName(exerciseObject.getString("name"));
                exercise.setReps(exerciseObject.getInt("reps"));
                exercise.setSets(exerciseObject.getInt("sets"));
                exercise.setCategory(exerciseObject.getString("category"));
                exercise.setDifficulty(exerciseObject.getInt("difficulty"));
                exercise.setiImageName(exerciseObject.getString("image_name"));
                exercise.setDetails(exerciseObject.getString("details"));

                exercises.add(exercise);
            }
            for (int i = 0; i < arrayPull.length(); i++) {
                JSONObject exerciseObject = arrayPull.getJSONObject(i);
                Exercise exercise = new Exercise();
                exercise.setId(exerciseObject.getString("id"));
                exercise.setDay("pull");
                exercise.setName(exerciseObject.getString("name"));
                exercise.setReps(exerciseObject.getInt("reps"));
                exercise.setSets(exerciseObject.getInt("sets"));
                exercise.setCategory(exerciseObject.getString("category"));
                exercise.setDifficulty(exerciseObject.getInt("difficulty"));
                exercise.setiImageName(exerciseObject.getString("image_name"));
                exercise.setDetails(exerciseObject.getString("details"));

                exercises.add(exercise);
            }
            for (int i = 0; i < arrayLegs.length(); i++) {
                JSONObject exerciseObject = arrayLegs.getJSONObject(i);
                Exercise exercise = new Exercise();
                exercise.setId(exerciseObject.getString("id"));
                exercise.setDay("legs");
                exercise.setName(exerciseObject.getString("name"));
                exercise.setReps(exerciseObject.getInt("reps"));
                exercise.setSets(exerciseObject.getInt("sets"));
                exercise.setCategory(exerciseObject.getString("category"));
                exercise.setDifficulty(exerciseObject.getInt("difficulty"));
                exercise.setiImageName(exerciseObject.getString("image_name"));
                exercise.setDetails(exerciseObject.getString("details"));

                exercises.add(exercise);
            }

        } catch (JSONException e) {
            Log.e("jsonread error", e.getMessage());
        }
        return exercises;
    }
}
