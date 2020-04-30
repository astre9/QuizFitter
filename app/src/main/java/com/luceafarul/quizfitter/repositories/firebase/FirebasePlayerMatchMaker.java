package com.luceafarul.quizfitter.repositories.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.luceafarul.quizfitter.models.Challenge;
import com.luceafarul.quizfitter.models.Match;

public class FirebasePlayerMatchMaker {

    public static final String QUEUE_REF = "/queues";
    public static final String MATCHES_REF = "/matches";

    private DatabaseReference userQueueRef;
    private OnMatchMadeCallback onFinished;

    public String starter;
    public String opponent;
    public String matchPath;
    public int localUserIndex;
    private boolean isFinished = false;
    protected boolean isThisStarter;

    protected Matcher matcher;
    protected SelfChallengeManager selfChallengeManager;
    protected SelfChallengeCanceller selfChallengeCanceller;

    public interface OnMatchMadeCallback {
        public void startMatch(FirebasePlayerMatchMaker c);
    }

    private FirebasePlayerMatchMaker(DatabaseReference userRoomRef, OnMatchMadeCallback onComplete) {
        userQueueRef = userRoomRef;
        onFinished = onComplete;
    }

    public static FirebasePlayerMatchMaker newInstance(OnMatchMadeCallback onComplete) {
        return new FirebasePlayerMatchMaker(FirebaseDatabase.getInstance().getReference(QUEUE_REF), onComplete);
    }

    public void findMatch() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OnFailCallback onMatchNotFoundFallback = new OnFailCallback() {
                    @Override
                    public void onFail() {
                        matcher = null;
                        selfChallengeManager = new SelfChallengeManager();
                        userQueueRef.runTransaction(selfChallengeManager);
                    }
                };
                matcher = new Matcher(onMatchNotFoundFallback);
                userQueueRef.runTransaction(matcher);
            }
        }).start();
    }

    public void stop() {
        if (selfChallengeManager == null || selfChallengeCanceller != null) {
            return;
        }

        selfChallengeCanceller = new SelfChallengeCanceller(selfChallengeManager);
        userQueueRef.runTransaction(selfChallengeCanceller);
    }

    public boolean hasEnded() {
        return isFinished;
    }

    protected void onMatchFound(boolean isStarter) {
        isThisStarter = isStarter;
        localUserIndex = (isStarter) ? 1 : 0;
        onFinished.startMatch(this);
    }

    public boolean isThisStarter() {
        return isThisStarter;
    }

    public interface OnFailCallback {
        public void onFail();
    }

    protected class Matcher implements Transaction.Handler {

        private Challenge selectedChallenge = null;
        private final OnFailCallback failCallback;
        private DatabaseReference matchesRef;

        protected Matcher(@Nullable OnFailCallback failCallback) {
            this.failCallback = failCallback;
        }

        @NonNull
        @Override
        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
            for (MutableData challengeData : mutableData.getChildren()) {
                final Challenge postedChallenge = challengeData.getValue(Challenge.class);

                if (isChallengeCompat(postedChallenge)) {
                    selectedChallenge = new Challenge(FirebaseAuth.getInstance().getCurrentUser().getUid(), postedChallenge.matchRef);
                    selectedChallenge.setOpponent(postedChallenge.starter);
                    opponent = selectedChallenge.opponent;
                    starter = selectedChallenge.starter;
//                    postedChallenge.setOpponent(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    matchesRef = FirebaseDatabase.getInstance().getReference(selectedChallenge.matchRef);
                    matchesRef.child("opponent").setValue(starter);
                    challengeData.child("opponent").setValue(starter);
//                    challengeData.setValue(null);
                    Log.d("MatchMaker.Matcher", "From opponent user: Matched " + starter + " with " + opponent);
                    return Transaction.success(mutableData);
                }
            }

            Log.d("MatchMaker.Matcher", "Didn't find any matching challenge");
            return Transaction.success(mutableData);
        }

        @Override
        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
            if (selectedChallenge != null) {
                starter = selectedChallenge.starter;
                opponent = selectedChallenge.opponent;
                matchPath = selectedChallenge.matchRef;
                Log.d("MatchMaker.Matcher", "Found match, onComplete");
                onMatchFound(false);
            } else if (failCallback != null) {
                failCallback.onFail();
            }
        }

        public boolean isChallengeCompat(Challenge challenge) {
            if (challenge.opponent == null) {
                return true;
            } else {
                return false;
            }
        }
    }

    protected class SelfChallengeManager implements Transaction.Handler, ValueEventListener {

        protected final Challenge publishedChallenge;
        protected final Match publishedMatch;
        protected DatabaseReference queuesRef;
        protected DatabaseReference matchesRef;

        protected SelfChallengeManager() {
            publishedChallenge = new Challenge(FirebaseAuth.getInstance().getCurrentUser().getUid(), null);
            publishedMatch = new Match(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

        @NonNull
        @Override
        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
            matchesRef = FirebaseDatabase.getInstance().getReference()
                    .child(MATCHES_REF)
                    .push();
            publishedChallenge.matchRef = MATCHES_REF + "/" + matchesRef.getKey();
            matchPath = publishedChallenge.matchRef;

            matchesRef.setValue(publishedMatch);

            queuesRef = FirebaseDatabase.getInstance().getReference(QUEUE_REF + "/" + FirebaseAuth.getInstance().getUid());
            queuesRef.setValue(publishedChallenge);
            queuesRef.addValueEventListener(this);

            return Transaction.success(mutableData);
        }

        @Override
        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
            Log.d("MatchMaker", "Published player challenge");
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Challenge acceptedChallenge = dataSnapshot.getValue(Challenge.class);
            if (acceptedChallenge != null) {
                if (acceptedChallenge.opponent != null && acceptedChallenge.starter.equals(FirebaseAuth.getInstance().getUid())) {
                    starter = FirebaseAuth.getInstance().getUid();
                    opponent = acceptedChallenge.opponent;
                    queuesRef = null;
                    matchesRef = null;
                    selfChallengeManager = null;
                    Log.d("MatchMaker.Matcher", "From starter user: Matched " + starter + " with " + opponent);
                    onMatchFound(true);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d("MatchMaker", "Cancelled: " + databaseError.getMessage());
        }
    }

    protected class SelfChallengeCanceller implements Transaction.Handler {

        private final SelfChallengeManager challengeManager;

        private SelfChallengeCanceller(SelfChallengeManager challenger) {
            Log.d("MatchMaker.Cancel", "Opened cancel request");
            challengeManager = challenger;
        }

        @NonNull
        @Override
        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
            challengeManager.queuesRef.removeEventListener(challengeManager);
            final String challengeKey = challengeManager.queuesRef.getKey();

            for (MutableData challengeNode : mutableData.getChildren()) {
                if (challengeNode.getKey().contentEquals(challengeKey)) {
                    challengeNode.setValue(null);
                }
            }

            return Transaction.success(mutableData);
        }

        @Override
        public void onComplete(@Nullable DatabaseError databaseError, boolean b,
                               @Nullable DataSnapshot dataSnapshot) {
            challengeManager.queuesRef = null;
            final DatabaseReference gameRecordRef = challengeManager.matchesRef;

            if (gameRecordRef != null) {
                gameRecordRef.setValue(null);
            }
        }
    }
}

