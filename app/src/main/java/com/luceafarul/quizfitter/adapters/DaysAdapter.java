package com.luceafarul.quizfitter.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.luceafarul.quizfitter.R;
import com.luceafarul.quizfitter.interfaces.OnDaySelectedCallback;
import com.luceafarul.quizfitter.model.Day;
import com.luceafarul.quizfitter.model.FoodDetails;
import com.luceafarul.quizfitter.model.Meal;
import com.luceafarul.quizfitter.repositories.room.DataBase;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {


    private List<Day> days;
    private OnDaySelectedCallback callback;
    private Context context;
    FoodDetails details;
    String imageName;

    public DaysAdapter(List<Day> days, OnDaySelectedCallback callback) {
        this.days = days;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_day, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DaysAdapter.ViewHolder holder, int position) {
        Day day = days.get(position);

        CardView cardDay = holder.cardDay;
        TextView tvDate = holder.tvDate;
        TextView tvDetails = holder.tvDetails;
        TextView tvComment = holder.tvComment;
        ImageView ivThumbnail = holder.ivThumbnail;
//        Button btnFavourite = holder.btnFavourite;

        tvDate.setText(day.getDate());

        final Handler handler = new Handler();
        (new Thread(new Runnable() {
            @Override
            public void run() {
                DataBase dataBase = DataBase.getInstance(context);

                List<Meal> meals = dataBase.mealsDao().getMealsByDay(day.getId());

                for (Meal m : meals) {
                    details = dataBase.foodsDAO().getDetailsByMeal(m.getId());
                    if (m.getImageName() != null) {
                        imageName = m.getImageName();
                    }
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        cardDay.setTag(false);
                        tvDetails.setText(day.toString());
                        tvComment.setText(day.getComment());
                        tvDetails.setText(details.toString());
                        ivThumbnail.setBackground(getImageDay(imageName));
                        ivThumbnail.setRotation(90);
                        ivThumbnail.setScaleType(ImageView.ScaleType.CENTER);
                        cardDay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean isSelected = !Boolean.parseBoolean(cardDay.getTag().toString());
                                cardDay.setTag(isSelected);
                                setCardColorTran(cardDay);
                                setTextViewColorTran(tvDetails, isSelected);
                                setTextViewColorTran(tvDate, isSelected);
                                notifyItemChanged(position, isSelected);
                                callback.onDaySelected(day.getDate());
                            }
                        });
                    }
                });
            }
        })).start();

    }

    private BitmapDrawable getImageDay(String imageName) {
        String filePath = Environment.getExternalStorageDirectory() + "/QFMeals/" + imageName;
        BitmapDrawable bitmapDrawable;
        if (!new File(filePath).exists()) {
            final int imageId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageId);
            bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            bitmapDrawable = new BitmapDrawable(bitmap);
        }

        return bitmapDrawable;
    }

    private void setCardColorTran(CardView card) {
        ColorDrawable[] color = {new ColorDrawable(context.getResources().getColor(R.color.colorOnPrimary)), new ColorDrawable(context.getResources().getColor(R.color.colorAccent))};

        if (!Boolean.parseBoolean(card.getTag().toString())) {
            color[0] = new ColorDrawable(context.getResources().getColor(R.color.colorAccent));
            color[1] = new ColorDrawable(Color.WHITE);
        }
        TransitionDrawable trans = new TransitionDrawable(color);
        card.setBackground(trans);
        trans.startTransition(1000);
    }

    private void setTextViewColorTran(TextView textView, boolean isSelected) {
        if (isSelected) {
            textView.setTextColor(Color.WHITE);
        } else {
            textView.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDate;
        public CardView cardDay;
        public TextView tvDetails;
        public TextView tvComment;
        public ImageView ivThumbnail;
//        public Button btnFavourite;

        public ViewHolder(View itemView) {
            super(itemView);

            cardDay = itemView.findViewById(R.id.cardDay);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            tvComment = itemView.findViewById(R.id.tvComment);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
//            btnFavourite = itemView.findViewById(R.id.btnFavourite);
        }
    }
}
