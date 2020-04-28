package com.luceafarul.quizfitter;


import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.luceafarul.quizfitter.repositories.room.DataBase;

import io.realm.Realm;
import io.realm.RealmConfiguration;

// Clasa pentru configurare a instantei de Realm la nivelul intregii aplicatii

public class FQApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            DataBase.getInstance(getApplicationContext());
            Realm.init(getApplicationContext());
            RealmConfiguration config = new RealmConfiguration.Builder().name("user.realm").build();
            Realm.setDefaultConfiguration(config);
        } catch (Exception e) {
            Log.d("Eroare ROOM/Realm", e.getMessage());
        }
    }
}
