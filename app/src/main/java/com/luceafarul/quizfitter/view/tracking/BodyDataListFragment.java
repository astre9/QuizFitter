package com.luceafarul.quizfitter.view.tracking;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.model.UserDetails;
import com.luceafarul.quizfitter.adapters.BodyDataAdapter;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class BodyDataListFragment extends Fragment {

    private ListView lvBodyData;
    private BodyDataAdapter bodyDataAdapter;
    private List<UserDetails> list = new ArrayList<>();

    public interface OnDataReceiveCallback {
        void onDataReceived();
    }

    public BodyDataListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_body_data_list, container, false);
        lvBodyData = view.findViewById(R.id.lvBodyData);

        getData(() -> {
            bodyDataAdapter = new BodyDataAdapter(getActivity(), list);
            lvBodyData.setAdapter(bodyDataAdapter);
        });

        lvBodyData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("userDetails", list.get(position));
                Fragment frg = new AddBodyDataFragment();
                frg.setArguments(bundle);
                changeFragment(frg);
            }
        });
        return view;
    }

    private void getData(OnDataReceiveCallback callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDetailsRef = FirebaseDatabase.getInstance().getReference("usersDetails").child(user.getUid());

        ValueEventListener categoryListener = new ValueEventListener() {
            int i = 0;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    list.add(dsp.getValue(UserDetails.class));
                }
                callback.onDataReceived();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadUsersDetails:onCancelled", databaseError.toException());
            }
        };
        userDetailsRef.addListenerForSingleValueEvent(categoryListener);

    }

    public void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
