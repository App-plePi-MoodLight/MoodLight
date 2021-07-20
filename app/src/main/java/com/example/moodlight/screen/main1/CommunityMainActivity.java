package com.example.moodlight.screen.main1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moodlight.R;
import com.example.moodlight.databinding.ActivityCommunityMainBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.example.moodlight.screen.main1.CommunityActiviy.todayMood;

public class CommunityMainActivity extends AppCompatActivity {

    private ActivityCommunityMainBinding binding;
    private FirebaseFirestore db;
    private int postNumber;
    private ArrayList<CommunityItem> list;
    private CommunityAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_main);

        db = FirebaseFirestore.getInstance();

        binding = DataBindingUtil.setContentView(this,R.layout.activity_community_main);
        binding.setActivity(this);
        setColor();

        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        binding.answerList.setLayoutManager(manager);

        list = new ArrayList<>();
        adapter = new CommunityAdapter(list);
        binding.answerList.setAdapter(adapter);
        getPostNumber();

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
    public void setItem(String question, String answer, int heart, int comment, int mood){
        list.add(new CommunityItem(question,answer,heart,comment,mood));
        adapter.notifyDataSetChanged();
    }
    public void setAnswer(int name){
        db.collection("post")
                .document(String.valueOf(name))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();

                        String question = "";
                        String answer = String.valueOf(documentSnapshot.get("answer"));
                        int heart = 0;
                        int comment = 0;
                        int mood = Integer.parseInt(String.valueOf(documentSnapshot.get("todayMood")));

                        setItem(question,answer,heart,comment,mood);
                    }
                });
    }
    public void setlist(int name){
        list.add(null);
        adapter.notifyDataSetChanged();
        int j = name - 15;
        if (j < 0){
            j = 0;
        }
        Log.d(TAG, "setlist: "+j);
        for(int i = name; i > j ; i--){
            setAnswer(i);
        }
        list.remove(list.size()-1);
        adapter.notifyItemRemoved(list.size()-1);
        postNumber-=15;
    }

    public void getPostNumber(){
        db.collection("post")
                .document("information")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        postNumber =  Integer.parseInt(String.valueOf(documentSnapshot.get("postNumber")));
                        setlist(postNumber);
                    }
                });
    }
}

