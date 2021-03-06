package com.luceafarul.quizfitter.view.gamification;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Fade;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.model.Answer;
import com.luceafarul.quizfitter.model.Match;
import com.luceafarul.quizfitter.model.Question;
import com.luceafarul.quizfitter.repositories.room.operations.GetAnswersAsync;
import com.luceafarul.quizfitter.repositories.room.operations.GetQuestionAsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionCardFragment extends Fragment implements View.OnClickListener {
    Button[] answerButtons = new Button[4];
    TextView tvCategory;
    TextView tvQuestion;
    ImageView ivQuestion;
    TextView tvScore1;
    TextView tvScore2;
    ProgressBar pbQuestionTimer;
    List<Answer> answers = new ArrayList<>();
    View parentView;
    Question question;
    Match match;
    DatabaseReference matchesRef;
    String idPlayer1;
    String idPlayer2;
    String matchPath;
    boolean isQuestionAnswered;
    int questionNumber;
    int totalScore = 0;
    int questionScore = 10;
    int questionTime = 0;

    public QuestionCardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question_card, container, false);

        matchPath = getArguments().getString("matchPath");
        idPlayer1 = getArguments().getString("player1");
        idPlayer2 = getArguments().getString("player2");
        questionNumber = getArguments().getInt("question") > 0 ? getArguments().getInt("question") : 1;

        initViews(view);

        getCurrentMatchData(new QuizFragment.OnDataReceiveCallback() {
            @Override
            public void onDataReceived() {
                Log.d("QuestionCardFragment:getCurrentMatchData", match.opponentScore + " " + match.starterScore);
                totalScore = match.getScoreByUser(idPlayer1);
                tvScore1.setText(String.valueOf(match.getScoreByUser(idPlayer1)));
                tvScore2.setText(String.valueOf(match.getScoreByUser(idPlayer2)));

                getNextQuestion();
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btAnswer1:
                if (answers.get(0).isCorrect()) {
                    goToNextQuestion(true);
                } else {
                    goToNextQuestion(false);
                }
                break;

            case R.id.btAnswer2:
                if (answers.get(1).isCorrect()) {
                    goToNextQuestion(true);
                } else {
                    goToNextQuestion(false);
                }
                break;

            case R.id.btAnswer3:
                if (answers.get(2).isCorrect()) {
                    goToNextQuestion(true);
                } else {
                    goToNextQuestion(false);
                }
                break;

            case R.id.btAnswer4:
                if (answers.get(3).isCorrect()) {
                    goToNextQuestion(true);
                } else {
                    goToNextQuestion(false);
                }
                break;

            default:
                break;
        }
    }

    public void initViews(View view) {
        ivQuestion = view.findViewById(R.id.media_image);
        tvCategory = view.findViewById(R.id.tvCategory);
        tvQuestion = view.findViewById(R.id.tvQuestion);
        answerButtons[0] = view.findViewById(R.id.btAnswer1);
        answerButtons[1] = view.findViewById(R.id.btAnswer2);
        answerButtons[2] = view.findViewById(R.id.btAnswer3);
        answerButtons[3] = view.findViewById(R.id.btAnswer4);

        for (Button answerButton : answerButtons) {
            answerButton.setOnClickListener(this);
        }

        if (getParentFragment() != null) {
            parentView = getParentFragment().getView();
            tvScore1 = parentView.findViewById(R.id.tvScore1);
            tvScore2 = parentView.findViewById(R.id.tvScore2);
        }
    }

    private void goToNextQuestion(boolean isCorrectAnswer) {
        for (int i = 0; i < answers.size(); i++) {
            if (answers.get(i).isCorrect()) {
                answerButtons[i].setBackgroundColor(getResources().getColor(R.color.colorSuccess));
                answerButtons[i].setTextColor(getResources().getColor(R.color.colorBackground));
            } else {
                answerButtons[i].setBackgroundColor(getResources().getColor(R.color.colorDanger));
                answerButtons[i].setTextColor(getResources().getColor(R.color.colorBackground));
            }
        }
        if (isCorrectAnswer) {
            Toast.makeText(getActivity(), "Correct answer!!!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Wrong answer!!!", Toast.LENGTH_LONG).show();
            questionScore = 0;
        }
        isQuestionAnswered = true;
    }

    private void getNextQuestion() {
        new GetQuestionAsync(getContext()) {
            @Override
            protected void onPostExecute(Question newQuestion) {
                Log.d("QuestionCardFragment:getNextQuestion", String.valueOf(questionNumber));
                question = newQuestion;
                if (question != null) {
                    tvQuestion.setText(question.getText());
                    tvCategory.setText(question.getCategory());
                    Resources resources = getActivity().getResources();
                    int resourceId = resources.getIdentifier(question.getImageName(), "drawable",
                            getActivity().getPackageName());
                    ivQuestion.setImageResource(resourceId);
                    getAnswers();
                }
            }
        }.execute(questionNumber);
    }

    private void getAnswers() {
        new GetAnswersAsync(getContext()) {
            @Override
            protected void onPostExecute(List<Answer> newAnswers) {
                answers = newAnswers;
                setAnswers();
            }
        }.execute(questionNumber);
    }

    private void setAnswers() {
        answerButtons[0].setText(answers.get(0).getText());
        answerButtons[1].setText(answers.get(1).getText());
        if (answers.size() >= 3) {
            answerButtons[2].setText(answers.get(2).getText());
            answerButtons[3].setVisibility(View.VISIBLE);
        } else {
            answerButtons[2].setVisibility(View.GONE);
        }
        if (answers.size() == 4) {
            answerButtons[3].setText(answers.get(3).getText());
            answerButtons[3].setVisibility(View.VISIBLE);
        } else {
            answerButtons[3].setVisibility(View.GONE);
        }
        startTimer();
    }

    private void startTimer() {
        pbQuestionTimer = parentView.findViewById(R.id.pbQuestionTimer);

        int time = 10 * 1000;

        CountDownTimer cdt = new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {
                if (!isQuestionAnswered) {
                    questionScore -= 1;
                }
                questionTime++;
                pbQuestionTimer.setProgress(10 * questionTime, true);
            }

            public void onFinish() {
                if (isQuestionAnswered) {
                    int updatedScore = totalScore + questionScore;
                    Map<String, Object> matchScoreUpdates = new HashMap<>();
                    if (match.starter.equals(FirebaseAuth.getInstance().getUid())) {
                        matchScoreUpdates.put("starterScore", updatedScore);
                    } else {
                        matchScoreUpdates.put("opponentScore", updatedScore);
                    }
                    matchesRef.updateChildren(matchScoreUpdates);
                }
                if (questionNumber == 5) {
                    FirebaseDatabase.getInstance().getReference("queues/" + match.starter).setValue(null);
                    Fragment quizResultsFragment = new QuizResultsFragment();
                    match.starter = ((TextView) getParentFragment().getView().findViewById(R.id.tvPlayer1)).getText().toString();
                    match.opponent = ((TextView) getParentFragment().getView().findViewById(R.id.tvPlayer2)).getText().toString();
                    match.starterScore = Integer.valueOf(((TextView) getParentFragment().getView().findViewById(R.id.tvScore1)).getText().toString());
                    match.opponentScore = Integer.valueOf(((TextView) getParentFragment().getView().findViewById(R.id.tvScore2)).getText().toString());

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("match", match);
                    quizResultsFragment.setArguments(bundle);
                    performTransition(quizResultsFragment, bundle);
                } else {
                    performTransition(null, null);
                }
            }
        }.start();
    }

    private void performTransition(Fragment destinationFragment, Bundle bundle) {
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment currentQuestionFragment = destinationFragment != null ? getParentFragment() : fragmentManager.findFragmentById(R.id.fragment_container);
        Fragment nextQuestionFragment = destinationFragment != null ? destinationFragment : new QuestionCardFragment();

        if (bundle == null) {
            Bundle args = new Bundle();
            questionNumber++;
            args.putInt("question", questionNumber);
            args.putString("matchPath", matchPath);
            args.putString("player1", idPlayer1);
            args.putString("player2", idPlayer2);
            nextQuestionFragment.setArguments(args);
        } else {
            String player1 = ((TextView) getParentFragment().getView().findViewById(R.id.tvPlayer1)).getText().toString();
            String player2 = ((TextView) getParentFragment().getView().findViewById(R.id.tvPlayer2)).getText().toString();
            String scorePlayer1 = ((TextView) getParentFragment().getView().findViewById(R.id.tvScore1)).getText().toString();
            String scorePlayer2 = ((TextView) getParentFragment().getView().findViewById(R.id.tvScore2)).getText().toString();
            bundle.putString("player1", player1);
            bundle.putString("player2", player2);
            bundle.putInt("playerScore1", Integer.parseInt(scorePlayer1));
            bundle.putInt("playerScore2", Integer.parseInt(scorePlayer2));

            nextQuestionFragment.setArguments(bundle);
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // 1. Exit for Previous Fragment
        Fade exitFade = new Fade();
        exitFade.setDuration(300);
        currentQuestionFragment.setExitTransition(exitFade);

        // 2. Enter Transition for New Fragment
        Fade enterFade = new Fade();
        enterFade.setStartDelay(800);
        enterFade.setDuration(300);
        nextQuestionFragment.setEnterTransition(enterFade);

        fragmentTransaction.replace(R.id.fragment_container, nextQuestionFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void getCurrentMatchData(QuizFragment.OnDataReceiveCallback callback) {
        matchesRef = FirebaseDatabase.getInstance().getReference(matchPath);

        ValueEventListener matchesListener = new ValueEventListener() {
            int i = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                match = dataSnapshot.getValue(Match.class);
                if (match != null) {
                    callback.onDataReceived();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "QuizFragment:getCurrentMatchData", databaseError.toException());
            }
        };
        matchesRef.addListenerForSingleValueEvent(matchesListener);
    }
}
