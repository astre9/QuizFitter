package com.luceafarul.quizfitter.view.loading;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.view.users.LoginActivity;

public class SplashActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LottieCompositionFactory.fromRawRes(this, getResources().getIdentifier("lottie_quiz_finding", "raw", getPackageName()));
        LottieCompositionFactory.fromRawRes(this, getResources().getIdentifier("lottie_classify_exercise", "raw", getPackageName()));

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}