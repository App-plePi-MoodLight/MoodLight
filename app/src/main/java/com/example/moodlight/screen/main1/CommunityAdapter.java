package com.example.moodlight.screen.main1;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodlight.R;

import java.util.ArrayList;

import static com.example.moodlight.screen.main1.MainFragment1.todayMood;

public class CommunityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private ArrayList<CommunityItem> items;

    public CommunityAdapter(ArrayList<CommunityItem> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING :VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View vh = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_answer, parent, false);
            return new NormalViewHolder(vh);
        } else {
            View vh = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_loading, parent, false);
            return new LoadingViewHolder(vh);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NormalViewHolder){
            onBind((NormalViewHolder) holder, position);
        }else if (holder instanceof LoadingViewHolder){
        }
    }

    public void onBind(NormalViewHolder holder,int position){
        CommunityItem communityItem = items.get(position);
        holder.onBind(communityItem);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }
    public class NormalViewHolder extends RecyclerView.ViewHolder {
        private TextView todayQuestion;
        private TextView answer;
        private TextView heart;
        private TextView comment;
        private ImageButton heartBtn;
        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);
            todayQuestion = itemView.findViewById(R.id.todayQuestionAnswer);
            answer = itemView.findViewById(R.id.answer);
            heart = itemView.findViewById(R.id.heart);
            comment = itemView.findViewById(R.id.comment);
            heartBtn = itemView.findViewById(R.id.heartBtn);
        }
        public void onBind(CommunityItem item){
            todayQuestion.setText(item.getQuestion());
            answer.setText(item.getAnswer());
            heart.setText(String.valueOf(item.getHeart()));
            comment.setText(String.valueOf(item.getComment()));

            switch (item.getTodayMood()){
                case 0 :
                    heartBtn.setColorFilter(Color.parseColor("#f5cf66"));
                case 1:
                    heartBtn.setColorFilter(Color.parseColor("#ed5d4c"));
                    break;
                case 2:
                        heartBtn.setColorFilter(Color.parseColor("#10699e"));
                        break;
            }
        }
    }
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
