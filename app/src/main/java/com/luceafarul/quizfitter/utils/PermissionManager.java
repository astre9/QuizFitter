package com.luceafarul.quizfitter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import com.luceafarul.quizfitter.view.food.FoodHomeFragment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionManager {

    private Activity activity;

    public PermissionManager(Activity activity) {
        this.activity = activity;
    }

    public boolean checkPermissions(String[] permissions, int requestCode) {
        boolean permissionsGranted = false;
        if (ContextCompat.checkSelfPermission(activity, permissions[0]) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(activity, permissions[1]) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        } else {
            permissionsGranted = true;
        }

        return permissionsGranted;
    }
}
