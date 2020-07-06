package com.luceafarul.quizfitter.view.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.others.SharedPrefsFiles;
import com.luceafarul.quizfitter.view.HomeActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

// Activitate de login

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    private Button btnLogin;
    private Button btnRegister;
    private Button btnAdmin;
    private Button btnAdmin2;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvForgot;
    private Context that;
    private SharedPrefsFiles prefs;

    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        String newEmail = null;
        String newPassword = null;

        try {
            newEmail = getIntent().getExtras().getString("email");
            newPassword = getIntent().getExtras().getString("password");
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnAdmin = findViewById(R.id.btnAdmin);
        btnAdmin2 = findViewById(R.id.btnAdmin2);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etParola);
        tvForgot = findViewById(R.id.tvForgot);

        prefs = SharedPrefsFiles.getInstance(this);

        that = this;

        if (newEmail != null && newPassword != null) {
            etEmail.setText(newEmail);
            etPassword.setText(newPassword);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                signIn();
            }
        });

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = "astre999@gmail.com";
                password = "123456789";
                etEmail.setText(email);
                etPassword.setText(password);
                signIn();
            }
        });
        btnAdmin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = "astre99@gmail.com";
                password = "12345678";
                etEmail.setText(email);
                etPassword.setText(password);
                signIn();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(that, RegisterActivity.class);
                startActivity(intent);
            }
        });
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotIntent = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(forgotIntent);
            }
        });
        etPassword.setTransformationMethod(new PasswordTransformationMethod());
    }

    public boolean validate() {
        boolean valid = true;
        if (etEmail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
            valid = false;
        }
        return valid;
    }

    public void signIn() {
        if (validate()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("signInWithEmail", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                notifyLogin("Authentication successful!");
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("signInWithEmail", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } else {
            Toast.makeText(LoginActivity.this, "Invalid email or password.", Toast.LENGTH_SHORT).show();
        }
    }

    // metoda ce modifica UI in urma login-ului
    public void notifyLogin(String mesaj) {
        Toast.makeText(that, mesaj, Toast.LENGTH_SHORT).show();
        if (mesaj.equals("Authentication successful!")) {
            Intent homeIntent = new Intent(this, HomeActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            finish();
        }
    }
}
