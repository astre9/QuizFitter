package com.luceafarul.quizfitter.view.classification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.models.Exercise;
import com.luceafarul.quizfitter.others.TabAdapter;
import com.luceafarul.quizfitter.repositories.api.JSONRead;
import com.luceafarul.quizfitter.view.classification.tabfragments.FeedbackTabFragment;
import com.luceafarul.quizfitter.view.classification.tabfragments.ResultsTabFragment;
import com.luceafarul.quizfitter.view.classification.tabfragments.TabFragment;

import java.util.List;

public class ClassificationResultsActivity extends AppCompatActivity {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);
        String exercise = getIntent().getStringExtra("exercise");
        String imageName = getIntent().getStringExtra("imageName");

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabs);
        adapter = new TabAdapter(getSupportFragmentManager());

        Bundle bundleResultsTabFragment = new Bundle();
        bundleResultsTabFragment.putString("imageName", exercise);
        ResultsTabFragment resultsTabFragment = new ResultsTabFragment();
        resultsTabFragment.setArguments(bundleResultsTabFragment);

        Bundle bundleFeedbackTabFragment = new Bundle();
        bundleFeedbackTabFragment.putString("imageName", imageName);
        FeedbackTabFragment feedbackTabFragment = new FeedbackTabFragment();
        feedbackTabFragment.setArguments(bundleFeedbackTabFragment);
        adapter.addFragment(resultsTabFragment, "Results");
        adapter.addFragment(feedbackTabFragment, "Feedback");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
