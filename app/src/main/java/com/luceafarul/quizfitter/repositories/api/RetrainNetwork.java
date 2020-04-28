package com.luceafarul.quizfitter.repositories.api;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RetrainNetwork extends AsyncTask<String, Void, String> {

    static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/json; charset=utf-8");
    final OkHttpClient client = new OkHttpClient();
    Request request;

    @Override
    protected String doInBackground(String... args) {
        request = new Request.Builder()
                .url("http://192.168.100.7:8000/feedback?image_name=" + args[0] + "&category=" + args[1])
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, args[0]))
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return null;
            }
            return response.body().string();

        } catch (IOException e) {
            Log.e("retrain error", e.getMessage());
        }
        return null;
    }
}