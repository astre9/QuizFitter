package com.luceafarul.quizfitter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.model.UserDetails;

import java.util.List;

// Adaptor personalizat exercitii

public class BodyDataAdapter extends BaseAdapter {

    private Context context;
    private List<UserDetails> userDetailsList;
    private List<UserDetails> filteredData = null;
    private LayoutInflater inflater;

    public BodyDataAdapter(Context context, List<UserDetails> userDetailsList) {
        this.context = context;
        this.userDetailsList = userDetailsList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return userDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = inflater.inflate(R.layout.body_data_item, viewGroup, false);
        TextView tvDate = rowView.findViewById(R.id.tvDate);
        TextView tvBf = rowView.findViewById(R.id.tvBf);
        TextView tvWeight = rowView.findViewById(R.id.tvWeight);

        tvDate.setText(String.valueOf(userDetailsList.get(i).getDate()));
        tvBf.setText(userDetailsList.get(i).getBf() + "% bodyfat");
        tvWeight.setText(userDetailsList.get(i).getWeight() + "kg.");

        return rowView;
    }

    public void updateList(List<UserDetails> userDetailsList) {
        this.userDetailsList = userDetailsList;
        notifyDataSetChanged();
    }

    public List<UserDetails> getUserDetailsList() {
        return userDetailsList;
    }

}


