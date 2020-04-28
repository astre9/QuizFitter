package com.luceafarul.quizfitter.repositories.api;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClassifyExercise extends AsyncTask<String, Void, String> {

    static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/json; charset=utf-8");
    final OkHttpClient client = new OkHttpClient();
    Request request;

    @Override
    protected String doInBackground(String... filenames) {
        request = new Request.Builder()
                .url("http://192.168.100.5:8000/classify?image_name=" + filenames[0])
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, filenames[0]))
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return null;
            }
            return response.body().string();

        } catch (IOException e) {
            Log.e("classify error", e.getMessage());
        }
        return null;
    }
}