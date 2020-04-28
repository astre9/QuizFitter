package com.luceafarul.quizfitter.view.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.luceafarul.quizfitter.R;

public class ForgotActivity extends AppCompatActivity {

    EditText etEmail;
    Button btSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        View parentLayout = findViewById(R.id.clForgot);

        etEmail = findViewById(R.id.etEmail);
        btSend = findViewById(R.id.btnSend);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = etEmail.getText().toString().trim();
                if (isValidEmail(emailAddress)) {
                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("Email", "Email sent.");
                                        Toast.makeText(ForgotActivity.this, "Reset password email sent to " + emailAddress,
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ForgotActivity.this, "Invalid email.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Snackbar snackbar = Snackbar.make(view, "Invalid e-mail.", Snackbar.LENGTH_LONG)
                            .setActionTextColor(ContextCompat.getColor(ForgotActivity.this, R.color.colorPrimaryVariant));
                    snackbar.setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    View sbView = snackbar.getView();
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) sbView.getLayoutParams();
                    params.gravity = Gravity.TOP;
                    sbView.setLayoutParams(params);
                    sbView.setBackgroundColor(ContextCompat.getColor(ForgotActivity.this, R.color.colorDanger));
                    snackbar.show();
                }
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
