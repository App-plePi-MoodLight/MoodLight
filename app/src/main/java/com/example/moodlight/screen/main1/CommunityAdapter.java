package com.example.moodlight.screen.main1;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodlight.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        private FirebaseFirestore db;
        private int heartValue;
        private boolean isSelect = false;

        private TextView todayQuestion;
        private TextView answer;
        private TextView heart;
        private TextView comment;
        private ImageButton heartBtn;
        private Button commentBtn;

        public NormalViewHolder(@NonNull View itemView) {
            super(itemView);
            todayQuestion = itemView.findViewById(R.id.todayQuestionAnswer);
            answer = itemView.findViewById(R.id.answer);
            heart = itemView.findViewById(R.id.heart);
            comment = itemView.findViewById(R.id.comment);
            heartBtn = itemView.findViewById(R.id.heartBtn);
            commentBtn = itemView.findViewById(R.id.commentBtn);
        }
        public void onBind(CommunityItem item){
            db = FirebaseFirestore.getInstance();
            isSelect = item.getIsHeart() != 0 ? true : false;

            todayQuestion.setText(item.getTodayQuestion());
            answer.setText(item.getAnswer());
            heart.setText(String.valueOf(item.getHeart()));
            comment.setText(String.valueOf(item.getComment()));

            heartValue = item.getHeart();
            if (isSelect){
                heartBtn.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.heart_icon));
            }else{
                heartBtn.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.unselect_heart_icon));
            }

            switch (item.getTodayMood()){
                case 101:
                    heartBtn.setColorFilter(Color.parseColor("#f5cf66"));
                    break;
                case 102:
                    heartBtn.setColorFilter(Color.parseColor("#ed5d4c"));
                    break;
                case 103:
                        heartBtn.setColorFilter(Color.parseColor("#10699e"));
                        break;
            }

            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(),CommentActivity.class);
                    intent.putExtra("commentArray",item);
                    intent.putExtra("postNumber",item.getPostNumber());
                    itemView.getContext().startActivity(intent);
                }
            });

            Map<String, Object> map = new HashMap<>();
            heartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSelect){
                        map.put("heart",--heartValue);
                        map.put("isHeart",0);
                        heart.setText(String.valueOf(heartValue));
                        heartBtn.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.unselect_heart_icon));
                        db.collection("post")
                                .document(String.valueOf(item.getPostNumber()))
                                .update(map)
                                .addOnCompleteListener(task -> {
                                    if (!task.isSuccessful()){
                                        return;
                                    }
                                    isSelect = false;
                                });
                    }else{
                        map.put("heart",++heartValue);
                        map.put("isHeart",1);
                        heart.setText(String.valueOf(heartValue));
                        heartBtn.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.heart_icon));
                        db.collection("post")
                                .document(String.valueOf(item.getPostNumber()))
                                .update(map)
                                .addOnCompleteListener(task -> {
                                    if (!task.isSuccessful()){
                                        return;
                                    }
                                    isSelect = true;
                                });
                    }
                }
            });
        }
    }
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
