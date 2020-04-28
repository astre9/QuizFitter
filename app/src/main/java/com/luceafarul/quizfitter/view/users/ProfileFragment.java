package com.luceafarul.quizfitter.view.users;


import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;

import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.luceafarul.quizfitter.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    EditText etEmail;
    EditText etName;
    EditText etPassword;
    Button btSave;
    Button btDelete;

    String name, password, email;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        etEmail = view.findViewById(R.id.etEmail);
        etName = view.findViewById(R.id.etName);
        etPassword = view.findViewById(R.id.etNewPassword);

        etName.setFocusable(false);
        etName.setEnabled(false);
        etEmail.setFocusable(false);
        etEmail.setEnabled(false);
//        btSave = view.findViewById(R.id.btSave);
        btDelete = view.findViewById(R.id.btnDelete);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            email = user.getEmail();
            etEmail.setText(email);
            etName.setText(name);

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
        }

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("UserDelete", "User account deleted.");
                                    Toast.makeText(getContext(), "User account deleted!", Toast.LENGTH_SHORT).show();
                                    getActivity().finish();
                                    System.exit(0);
                                }
                            }
                        });
            }
        });

        return view;
    }
}
