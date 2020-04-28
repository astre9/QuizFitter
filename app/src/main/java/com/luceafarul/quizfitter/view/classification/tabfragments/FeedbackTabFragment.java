package com.luceafarul.quizfitter.view.classification.tabfragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.repositories.api.RetrainNetwork;
import com.luceafarul.quizfitter.view.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackTabFragment extends Fragment {

    TextView tvHello;
    TextView tvFeedback;
    Button btnSendFeedback;
    Button btnHome;
    RadioGroup rgFeedback;
    TextInputLayout tilExercise;
    AutoCompleteTextView editTextFilledExposedDropdown;
    boolean isCorrectClassification;
    String imageName;
    String category;

    public FeedbackTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback_tab, container, false);
        imageName = getArguments().getString("imageName");

        tvHello = view.findViewById(R.id.tvHello);
        tvFeedback = view.findViewById(R.id.tvFeedback);
        btnSendFeedback = view.findViewById(R.id.btnSend);
        btnHome = view.findViewById(R.id.btnCancel);
        editTextFilledExposedDropdown = view.findViewById(R.id.filled_exposed_dropdown);
        tilExercise = view.findViewById(R.id.tilExercise);
        String bicepEmoji = getEmojiByUnicode(0x1F4AA);
        String heartEmoji = getEmojiByUnicode(0x1F49C);

        tvHello.setText(tvHello.getText().toString().concat(bicepEmoji));
        tvFeedback.setText(tvFeedback.getText().toString().concat(heartEmoji));

        btnSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = tilExercise.getEditText().getText().toString().toLowerCase().replace(' ', '_');
                new RetrainNetwork().execute(imageName, category);
                Toast.makeText(getActivity(), "Thank you for your feedback!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });
        rgFeedback = (RadioGroup) view.findViewById(R.id.rgFeedback);
        rgFeedback.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbSelected = view.findViewById(checkedId);
                if (rbSelected.getTag().toString().equals("right")) {
                    isCorrectClassification = true;
                    tilExercise.setVisibility(View.INVISIBLE);
                } else {
                    isCorrectClassification = false;
                    tilExercise.setVisibility(View.VISIBLE);
                }
            }
        });

        String[] COUNTRIES = new String[]{"Squat", "Pull up", "Deadlift"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.material_spinner_item,
                        COUNTRIES);

        editTextFilledExposedDropdown.setAdapter(adapter);

        return view;
    }

    public String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }
}
