package com.example.moodlight.screen.main1;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.example.moodlight.util.DataTypeJava.HAPPY_MOOD;
import static com.example.moodlight.util.DataTypeJava.MAD_MOOD;
import static com.example.moodlight.util.DataTypeJava.SAD_MOOD;

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
        private boolean isLoading = false;

        private Map<String, Object> map;
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
            isHeart(item);

            map = new HashMap<>();
            todayQuestion.setText(item.getTodayQuestion());
            answer.setText(item.getAnswer());
            heart.setText(String.valueOf(item.getHeart()));
            comment.setText(String.valueOf(item.getComment()));


            switch (item.getMood()){
                case HAPPY_MOOD:
                    heartBtn.setColorFilter(Color.parseColor("#f5cf66"));
                    break;
                case MAD_MOOD:
                    heartBtn.setColorFilter(Color.parseColor("#ed5d4c"));
                    break;
                case SAD_MOOD:
                        heartBtn.setColorFilter(Color.parseColor("#10699e"));
                        break;
            }

            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(),CommentActivity.class);
                    intent.putExtra("comment",item);
                    intent.putExtra("postNumber",item.getPostNumber());
                    itemView.getContext().startActivity(intent);
                }
            });
            heartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSelect){
                        setHeart(item,--heartValue);
                    }else{
                        setHeart(item,++heartValue);
                    }
                }
            });
        }

        private void setHeart(CommunityItem item,int heartValue){
            map.put("heart",heartValue);
            heart.setText(String.valueOf(heartValue));
            if (isSelect){
                heartBtn.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.unselect_heart_icon));
            }else{
                heartBtn.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.heart_icon));
            }
            isSelect = !isSelect;
            db.collection("post")
                    .document(String.valueOf(item.getPostNumber()))
                    .update(map)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()){
                            return;
                        }
                        getHeart(item);
                    });
        }
        private void isHeart(CommunityItem item){
            db.collection("users")
                    .document(FirebaseAuth.getInstance().getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()){
                            return;
                        }
                        DocumentSnapshot documentSnapshot = task.getResult();
                        Map<String,Object> map = (Map<String, Object>) documentSnapshot.getData();
                        Map<String,Boolean> heartMap = (Map<String, Boolean>) map.get("isHeart");
                        try{
                            isSelect = heartMap.get(String.valueOf(item.getPostNumber()));
                        }catch (Exception e){
                            Log.d(TAG, "isHeart: "+e.getMessage());
                        }

                        heartValue = item.getHeart();
                        if (isSelect){
                            heartBtn.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.heart_icon));
                        }else{
                            heartBtn.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),R.drawable.unselect_heart_icon));
                        }
                    });
        }
        private void getHeart(CommunityItem item){
            if (!isLoading){
                isLoading = true;
                db.collection("users")
                        .document(FirebaseAuth.getInstance().getUid())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()){
                                return;
                            }
                            DocumentSnapshot documentSnapshot = task.getResult();
                            Map<String,Object> map = (Map<String, Object>) documentSnapshot.getData();

                            updateisHeart(item.getPostNumber(),map);
                        });
            }
        }
        private void updateisHeart(int id ,Map<String,Object> map){
            Map<String,Boolean> heartMap = (Map<String, Boolean>) map.get("isHeart");
            if (map.get("isHeart")==null){
                heartMap = new HashMap<>();
            }
            heartMap.put(String.valueOf(id),isSelect);
            map.put("isHeart",heartMap);
            db.collection("users")
                    .document(FirebaseAuth.getInstance().getUid())
                    .update(map)
                    .addOnCompleteListener(task -> {
                        isLoading = false;
                        if (!task.isSuccessful()){
                            return;
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

