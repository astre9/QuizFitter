package com.luceafarul.quizfitter.view.users;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.transition.TransitionManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.view.tracking.BodyDataListFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    EditText etEmail;
    EditText etName;
    TextView tvName;
    TextView tvMail;
    TextInputLayout tilMail;
    TextInputLayout tilName;
    EditText etPassword;
    Button btnSave;
    Button btnBodyData;
    MaterialButtonToggleGroup btnExpand;

    String name, password, email;
    int rotationAngle = 0;
    boolean isExpanded = false;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        CardView card = view.findViewById(R.id.cardProfile);
        ConstraintLayout clSvgs = view.findViewById(R.id.clSvgs);
        LinearLayout llStatsHeaders = view.findViewById(R.id.llStatsHeaders);
        LinearLayout llStatsValues = view.findViewById(R.id.llStatsValues);

        btnSave = view.findViewById(R.id.btnSave);
        btnBodyData = view.findViewById(R.id.btnBodyData);
        btnExpand = view.findViewById(R.id.btnExpand);
        tvMail = view.findViewById(R.id.tvEmail);
        etEmail = view.findViewById(R.id.etEmail);
        tilMail = view.findViewById(R.id.tilMail);
        tvName = view.findViewById(R.id.tvUsername);
        etName = view.findViewById(R.id.etName);
        tilName = view.findViewById(R.id.tilName);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            email = user.getEmail();
            tvMail.setText(email);
            tvName.setText(name);

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
        }

        btnExpand.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                ObjectAnimator anim = ObjectAnimator.ofFloat(btnExpand, "rotation", rotationAngle, rotationAngle + 180);
                anim.setDuration(500);
                anim.start();
                rotationAngle += 180;
                rotationAngle = rotationAngle % 360;
                TransitionManager.beginDelayedTransition(card);
                TransitionManager.beginDelayedTransition(clSvgs);

                isExpanded = !isExpanded;
                if (isExpanded) {
                    expandCard(card, clSvgs);
                } else {
                    collapseCard(card, clSvgs);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        btnBodyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new BodyDataListFragment());
            }
        });

        return view;
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void expandCard(CardView card, ConstraintLayout clSvgs) {
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0f
        );
        card.setLayoutParams(cardParams);
        LinearLayout.LayoutParams clParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                0f
        );
        clSvgs.setLayoutParams(clParams);
        tilMail.setVisibility(View.VISIBLE);
        tilName.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.VISIBLE);
        etEmail.setText(email);
        etName.setText(name);
    }

    private void collapseCard(CardView card, ConstraintLayout clSvgs) {
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                0.35f
        );
        card.setLayoutParams(cardParams);
        LinearLayout.LayoutParams clParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                0.65f
        );
        clSvgs.setLayoutParams(clParams);
        tilName.setVisibility(View.GONE);
        tilMail.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
    }

    private void updateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Get auth credentials from the user for re-authentication
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), "12345678"); // TODO Fix password management
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("Profile:updateProfile", "User re-authenticated.");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.updateEmail(etEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("UpdateProfile:complete", "User email address updated.");
                            Toast.makeText(getActivity(), "Profile info updated!", Toast.LENGTH_LONG).show();
                            btnExpand.callOnClick();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("UpdateProfile:fail", e.getMessage());
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(etName.getText().toString()).build();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("UpdateName:complete", "Success update display name");
                            }
                        });
            }
        });
    }
}
