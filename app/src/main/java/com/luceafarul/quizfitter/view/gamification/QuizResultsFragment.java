package com.luceafarul.quizfitter.view.gamification;


import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.model.Match;
import com.luceafarul.quizfitter.view.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizResultsFragment extends Fragment {

    public static final int QUIZ_REQUEST_CODE = 100;

    TextView tvResult;
    TextView tvScore;
    TextView tvScorePlayer1;
    TextView tvScorePlayer2;
    TextView tvPlayer1;
    TextView tvPlayer2;
    Button btnHome;
    TextView btnQuizAgain;

    public QuizResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        initViews(view);

        if (getArguments() != null) {
            Match match = getArguments().getParcelable("match");
            boolean isWinner = true;
            if (match.starterScore < match.opponentScore) {
                isWinner = false;
            }
            setViews(match, isWinner);
        }

        btnQuizAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizResultsFragment.this.getActivity(), HomeActivity.class);
                startActivityForResult(intent, QUIZ_REQUEST_CODE);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizResultsFragment.this.getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void initViews(View view) {
        tvScore = view.findViewById(R.id.tvScore);
        tvResult = view.findViewById(R.id.tvResult);
        tvPlayer1 = view.findViewById(R.id.tvPlayer1);
        tvPlayer2 = view.findViewById(R.id.tvPlayer2);
        tvScorePlayer1 = view.findViewById(R.id.tvScorePlayer1);
        tvScorePlayer2 = view.findViewById(R.id.tvScorePlayer2);
        btnHome = view.findViewById(R.id.btnHome);
        btnQuizAgain = view.findViewById(R.id.btnQuizAgain);
    }

    private void setViews(Match match, boolean isWinner) {
        if (isWinner) {
            tvResult.setText("YOU WON");
            tvScore.setTextColor(getResources().getColor(R.color.colorSuccess));
            tvResult.setTextColor(getResources().getColor(R.color.colorSuccess));
            tvPlayer1.setTextColor(getResources().getColor(R.color.colorSuccess));
            tvPlayer2.setTextColor(getResources().getColor(R.color.colorSuccess));
            tvScorePlayer1.setTextColor(getResources().getColor(R.color.colorSuccess));
            tvScorePlayer2.setTextColor(getResources().getColor(R.color.colorSuccess));
        } else {
            tvResult.setText("YOU LOST");
            tvScore.setTextColor(getResources().getColor(R.color.colorDanger));
            tvResult.setTextColor(getResources().getColor(R.color.colorDanger));
            tvPlayer1.setTextColor(getResources().getColor(R.color.colorDanger));
            tvPlayer2.setTextColor(getResources().getColor(R.color.colorDanger));
            tvScorePlayer1.setTextColor(getResources().getColor(R.color.colorDanger));
            tvScorePlayer2.setTextColor(getResources().getColor(R.color.colorDanger));
        }

        tvPlayer1.setText(match.starter);
        tvPlayer2.setText(match.opponent);
        tvScorePlayer1.setText(String.valueOf(match.starterScore));
        tvScorePlayer2.setText(String.valueOf(match.opponentScore));
    }

}
