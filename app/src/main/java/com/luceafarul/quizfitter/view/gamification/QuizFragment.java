package com.luceafarul.quizfitter.view.gamification;


import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.model.Challenge;
import com.luceafarul.quizfitter.model.User;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment {
    TextView tvPlayer1;
    TextView tvPlayer2;
    DatabaseReference usersReference;
    String idPlayer1;
    String idPlayer2;
    String matchPath;
    Challenge challenge;
    User localUser;
    User opponentUser;
    MaterialCardView card;
    FragmentManager fragmentManager;

    public interface OnDataReceiveCallback {
        void onDataReceived();
    }

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        initViews(view);

        usersReference = FirebaseDatabase.getInstance().getReference("users");

        if (getArguments() != null) {
            matchPath = getArguments().getString("matchPath");
            idPlayer1 = getArguments().getString("player1");
            idPlayer2 = getArguments().getString("player2");

            getPlayersData(new OnDataReceiveCallback() {
                @Override
                public void onDataReceived() {
                    tvPlayer1.setText(localUser.getUsername());
                    tvPlayer2.setText(opponentUser.getUsername());

                    Fragment questionCardFragment = new QuestionCardFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("matchPath", matchPath);
                    bundle.putString("player1", idPlayer1);
                    bundle.putString("player2", idPlayer2);
                    questionCardFragment.setArguments(bundle);
                    changeFragment(questionCardFragment);
                }
            });
        }


        return view;
    }

    private void initViews(View view) {
        card = view.findViewById(R.id.quizCard);
        tvPlayer1 = view.findViewById(R.id.tvPlayer1);
        tvPlayer2 = view.findViewById(R.id.tvPlayer2);
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getPlayersData(OnDataReceiveCallback callback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        ValueEventListener usersListener = new ValueEventListener() {
            int i = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    User player = dsp.getValue(User.class);
                    if (player.getId().equals(idPlayer1)) {
                        localUser = player;
                    } else if (player.getId().equals(idPlayer2)) {
                        opponentUser = player;
                    }
                }
                callback.onDataReceived();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "QuizFragment:getPlayersData", databaseError.toException());
            }
        };
        usersRef.addListenerForSingleValueEvent(usersListener);
    }
}
