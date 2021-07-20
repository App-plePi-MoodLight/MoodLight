package com.example.moodlight.screen.main1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moodlight.R;
import com.example.moodlight.databinding.ActivityCommunityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.example.moodlight.screen.main1.CommunityActiviy.todayMood;

public class CommunityMainActivity extends AppCompatActivity {

    private ActivityCommunityMainBinding binding;
    private FirebaseFirestore db;
    private int postNumber;
    private ArrayList<CommunityItem> list;
    private CommunityAdapter adapter;
    private DocumentSnapshot lastVisibleDocument;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_main);

        lastVisibleDocument = null;

        db = FirebaseFirestore.getInstance();

        binding = DataBindingUtil.setContentView(this,R.layout.activity_community_main);
        binding.setActivity(this);
        setColor();

        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        binding.answerList.setLayoutManager(manager);

        list = new ArrayList<>();
        adapter = new CommunityAdapter(list);
        binding.answerList.setAdapter(adapter);
        setlist();



    }
    private void setColor(){
        switch (todayMood){
            case 0: binding.answerBtn.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_happy_background));
                break;
            case 1:
                binding.answerBtn.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_mad_background));
                break;
            case 2:
                binding.answerBtn.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_sad_background));
                break;
        }
    }
    public void intentAnswer(View view){
        Intent intent = new Intent(this,AnswerActivity.class);
        startActivity(intent);
    }
    private void setItem(String question, String answer, int heart, int comment, int mood){
        list.add(new CommunityItem(question,answer,heart,comment,mood,0,0));
        adapter.notifyDataSetChanged();
    }
    private Query setAnswer(){
        CollectionReference collectionReference = db.collection("post");
        if (lastVisibleDocument == null){
            return collectionReference.whereEqualTo("todayMood",todayMood).limit(20);
        }
        return collectionReference.whereEqualTo("todayMood",todayMood).startAfter(lastVisibleDocument).limit(20);
    }
    private void setlist(){
        list.add(null);
        adapter.notifyDataSetChanged();
        Query query = setAnswer();
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Log.d(TAG, "setlist: 성공");
                for (DocumentSnapshot documentSnapshot : task.getResult()){
                    CommunityItem item = documentSnapshot.toObject(CommunityItem.class);
                    list.add(item);
                }
                adapter.notifyDataSetChanged();
                try{
                    lastVisibleDocument = task.getResult().getDocuments().get(task.getResult().size()-1);
                }catch (Exception e){
                    e.getMessage();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: 실패"+e.getMessage());
            }
        });
        list.remove(list.size()-1);
        adapter.notifyItemRemoved(list.size()-1);
    }


    public void finishActvity(View view){
        todayMood = 3;
        finish();
    }
}

