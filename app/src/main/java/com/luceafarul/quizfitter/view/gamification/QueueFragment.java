package com.luceafarul.quizfitter.view.gamification;


import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.repositories.firebase.FirebasePlayerMatchMaker;
import com.luceafarul.quizfitter.view.HomeFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class QueueFragment extends Fragment {
    Button btnCancel;
    DatabaseReference queuesRef;
    DatabaseReference matchesRef;
    FirebasePlayerMatchMaker firebasePlayerMatchMaker;

    public QueueFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_queue, container, false);

        btnCancel = view.findViewById(R.id.btnCancel);
        queuesRef = FirebaseDatabase.getInstance().getReference("queues");
        matchesRef = FirebaseDatabase.getInstance().getReference("matches");


        firebasePlayerMatchMaker = FirebasePlayerMatchMaker.newInstance(new FirebasePlayerMatchMaker.OnMatchMadeCallback() {
            @Override
            public void startMatch(FirebasePlayerMatchMaker c) {
                Bundle bundle = new Bundle();
                bundle.putString("matchPath", c.matchPath);
                bundle.putString("player1", FirebaseAuth.getInstance().getUid());
                bundle.putString("player2", FirebaseAuth.getInstance().getUid().equals(c.starter) ? c.opponent : c.starter);
                QuizFragment quizFragment = new QuizFragment();
                quizFragment.setArguments(bundle);
                changeFragment(quizFragment);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebasePlayerMatchMaker.stop();
                changeFragment(new HomeFragment());
            }
        });

        firebasePlayerMatchMaker.findMatch();

        return view;
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
