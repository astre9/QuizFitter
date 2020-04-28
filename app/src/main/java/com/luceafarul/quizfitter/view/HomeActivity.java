package com.luceafarul.quizfitter.view;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.luceafarul.quizfitter.R;
import com.google.android.material.navigation.NavigationView;
import com.luceafarul.quizfitter.others.SharedPrefsFiles;
import com.luceafarul.quizfitter.view.classification.CameraActivity;
import com.luceafarul.quizfitter.view.gamification.QueueFragment;
import com.luceafarul.quizfitter.view.tracking.BodyDataListFragment;
import com.luceafarul.quizfitter.view.tracking.FoodListFragment;
import com.luceafarul.quizfitter.view.users.LoginActivity;
import com.luceafarul.quizfitter.view.users.ProfileFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int APPLICATION_PERMISSION_CODE = 100;

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

        configNavigation();
        changeFragment(new HomeFragment());
    }

    private void configNavigation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.home_drawer_layout);
        ActionBarDrawerToggle actionBar =
                new ActionBarDrawerToggle(
                        this,
                        drawerLayout,
                        toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navHeader = navigationView.getHeaderView(0);
        TextView tvNavUser = navHeader.findViewById(R.id.tvNavUser);
        tvNavUser.setText("Welcome, " + email);
        actionBar.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home: {
                changeFragment(new HomeFragment());
                break;
            }
            case R.id.nav_profile: {
                changeFragment(new ProfileFragment());
                break;
            }
            case R.id.nav_body_data: {
                changeFragment(new BodyDataListFragment());
                break;
            }
            case R.id.nav_food: {
                sharedPrefs.saveString("selectedFoodId", "");
                changeFragment(new FoodListFragment());
                break;
            }
            case R.id.nav_play: {
                changeFragment(new QueueFragment());
                break;
            }
            case R.id.nav_gym: {
                changeFragment(new ExercisesFragment());
                break;
            }
            case R.id.nav_camera: {
                String[] permissions = new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA};
                if (checkPermissions(permissions, APPLICATION_PERMISSION_CODE)) {
                    Intent intent = new Intent(this, CameraActivity.class);
                    startActivity(intent);
                }

                break;
            }
            case R.id.nav_logout: {
                Logout();
                break;
            }
//            case R.id.nav_bmi: {
//                changeFragment(new WIPFragment());
//                break;
//            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
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
}
