package com.luceafarul.quizfitter.view.classification.tabfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luceafarul.quizfitter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {

    public TabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);
        return view;
    }
}
