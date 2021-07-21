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
import androidx.recyclerview.widget.RecyclerView;

import com.example.moodlight.R;
import com.example.moodlight.databinding.ActivityCommunityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
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
            case 101: binding.answerBtn.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_happy_background));
                break;
            case 102:
                binding.answerBtn.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_mad_background));
                break;
            case 103:
                binding.answerBtn.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_sad_background));
                break;
        }
    }
    public void intentAnswer(View view){
        Intent intent = new Intent(this,AnswerActivity.class);
        startActivity(intent);
    }

    private Query setAnswer(){
        CollectionReference collectionReference = db.collection("post");
        if (lastVisibleDocument == null){
            return collectionReference.whereEqualTo("mood",todayMood).limit(20);
        }
        return collectionReference.whereEqualTo("mood",todayMood).startAfter(lastVisibleDocument).limit(20);
    }
    private void setlist(){
        list.add(null);
        adapter.notifyDataSetChanged();
        Query query = setAnswer();
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Log.d(TAG, "setlist: 성공"+modifyJoinTime());
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
            }
        });
        list.remove(list.size()-1);
        adapter.notifyItemRemoved(list.size()-1);
    }


    public void finishActvity(View view){
        todayMood = 3;
        finish();
    }

    private static String modifyJoinTime () {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String time = simpleDateFormat.format(System.currentTimeMillis());
        String[] timeArray = time.split("\\.");
        return timeArray[1]+"."+(Integer.parseInt(timeArray[2])+1);
    }
}

