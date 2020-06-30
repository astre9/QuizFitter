package com.luceafarul.quizfitter.view;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.others.SharedPrefsFiles;
import com.luceafarul.quizfitter.view.classification.CameraActivity;
import com.luceafarul.quizfitter.view.food.FoodHomeFragment;
import com.luceafarul.quizfitter.view.gamification.QuizResultsFragment;
import com.luceafarul.quizfitter.view.training.ExercisesListFragment;
import com.luceafarul.quizfitter.view.users.LoginActivity;
import com.luceafarul.quizfitter.view.users.ProfileFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

public class HomeActivity extends AppCompatActivity {//} implements NavigationView.OnNavigationItemSelectedListener {

    private static final int APPLICATION_PERMISSION_CODE = 100;
    private static final int FOOD_PERMISSION_CODE = 200;

    private DrawerLayout drawerLayout;
    private TextView tvNavUser;
    private TextView tvMail;
    private ImageView ivProfile;
    private SharedPrefsFiles sharedPrefs;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPrefs = SharedPrefsFiles.getInstance(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.itHome);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itExercise: {
                        changeFragment(new ExercisesListFragment());
                        break;
                    }
                    case R.id.itCamera: {
                        String[] permissions = new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA};
                        if (checkPermissions(permissions, APPLICATION_PERMISSION_CODE)) {
                            Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
                            startActivity(intent);
                        }
                        break;
                    }
                    case R.id.itHome: {
                        changeFragment(new HomeFragment());
                        break;
                    }

                    case R.id.itFood: {
                        String[] permissions = new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA};
                        if (checkPermissions(permissions, FOOD_PERMISSION_CODE)) {
                            changeFragment(new FoodHomeFragment());
                        }
                        break;
                    }

                    case R.id.itProfile: {
                        changeFragment(new ProfileFragment());
                        break;
                    }
                }
                return true;
            }
        });
        changeFragment(new HomeFragment());
    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean checkPermissions(String[] permissions, int requestCode) {
        boolean permissionsGranted = false;
        if (ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, permissions[1]) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        } else {
            permissionsGranted = true;
        }

        return permissionsGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == APPLICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, CameraActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Permissions denied, can't access exercise recognition!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QuizResultsFragment.QUIZ_REQUEST_CODE && resultCode == RESULT_OK) {
            changeFragment(new QuizResultsFragment());
        }
    }
}
