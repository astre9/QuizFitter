package com.luceafarul.quizfitter.view.tracking;


import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.model.UserDetails;
import com.luceafarul.quizfitter.others.SharedPrefsFiles;
import com.luceafarul.quizfitter.view.HomeFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddBodyDataFragment extends Fragment {

    EditText etWeight;
    EditText etBF;
    EditText etDate;
    Button btSave;
    Button btCancel;
    String selectedId = "";
    String parsedDate;
    Date date;
    DateFormat dateFormat;

    double weight, bf;

    public AddBodyDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_body_data, container, false);

        etDate = view.findViewById(R.id.etDate);
        etBF = view.findViewById(R.id.etBF);
        etWeight = view.findViewById(R.id.etWeight);
        btSave = view.findViewById(R.id.btnSave);
        btCancel = view.findViewById(R.id.btnCancel);

        etDate.setFocusable(false);
        etDate.setEnabled(false);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = new Date();

        if (getArguments() != null) {
            UserDetails usrDet = getArguments().getParcelable("userDetails");
            selectedId = usrDet.getId();
            etDate.setText(usrDet.getDate());
            etWeight.setText(String.valueOf(usrDet.getWeight()));
            etBF.setText(String.valueOf(usrDet.getBf()));
            btSave.setText("Update");
        } else {
            etDate.setText(dateFormat.format(date));
            btSave.setText("Save");
        }

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bf = Double.parseDouble(etBF.getText().toString());
                weight = Double.parseDouble(etWeight.getText().toString());
                parsedDate = etDate.getText().toString();

                SharedPrefsFiles prefs = SharedPrefsFiles.getInstance(getActivity());
                prefs.saveString("UpdatedTodayData", "yes");

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("usersDetails");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (selectedId.equals("")) {
                    UserDetails userDetails = new UserDetails(parsedDate, bf, weight, user.getUid());
                    ref.child(user.getUid()).child(userDetails.getId()).setValue(userDetails);
                } else {
                    UserDetails userDetails = new UserDetails(parsedDate, bf, weight, user.getUid());
                    ref.child(user.getUid()).child(selectedId).setValue(userDetails);
                }
                Toast.makeText(getActivity(), "Weight and body fat saved for " + dateFormat.format(date), Toast.LENGTH_SHORT).show();
                changeFragment(new HomeFragment());
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
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
}
