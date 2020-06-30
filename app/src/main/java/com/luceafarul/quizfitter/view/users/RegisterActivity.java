package com.luceafarul.quizfitter.view.users;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.model.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    Button btnRegister;
    Button btnBack;
    EditText etPassword;
    EditText etCheckPassword;
    EditText etEmail;
    EditText etName;

    String email;
    String name;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);
        etPassword = findViewById(R.id.etNewPassword);
        etCheckPassword = findViewById(R.id.etPasswordCheck);
        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString().trim();
                name = etName.getText().toString().trim();
                password = etPassword.getText().toString().trim();

                if (validate()) {
                    createAccount();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed creating account!", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        etPassword.setTransformationMethod(new PasswordTransformationMethod());
        etCheckPassword.setTransformationMethod(new PasswordTransformationMethod());
    }

    // metoda de validare pentru creare cont nou
    public boolean validate() {
        boolean result = true;
        if (etPassword.getText().toString().equals("") || etPassword.getText() == null || etPassword.getText().length() < 6) {
            etPassword.setError("Password must have more than 6 characters!");
            result = false;
        } else {
            etPassword.setError(null);
        }

        if (etEmail.getText().toString().equals("") || etEmail.getText() == null || etEmail.getText().length() < 6) {
            etEmail.setError("E-mail must have more than 6 characters!");
            result = false;
        }
        if (etCheckPassword.getText().toString().equals("") || etCheckPassword.getText() == null || etCheckPassword.getText().length() < 6 || !etCheckPassword.getText().toString().equals(etPassword.getText().toString())) {
            etCheckPassword.setError("Password doesn't match!");
            result = false;
        } else {
            etCheckPassword.setError(null);
        }
        return result;
    }

    // metoda de modificare a UI in urma incercarii de creeare a unui nou cont
    public void notifyRegister(String mesaj) {
        Toast.makeText(this, mesaj, Toast.LENGTH_LONG).show();
        if (mesaj.equals("Account created successfully!")) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.putExtra("email", email);
            loginIntent.putExtra("password", password);
            finish();
            startActivity(loginIntent);
        }
    }

    public void createAccount() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("createUserWithEmail", "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                            User myUser = new User(user.getUid(), name, password, email);
                            ref.child(user.getUid()).setValue(myUser);

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();
                            user.updateProfile(profileUpdates);

                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("Email", "Email sent.");
                                            }
                                        }
                                    });

                            notifyRegister("Account created successfully!");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("createUserWithEmail", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
