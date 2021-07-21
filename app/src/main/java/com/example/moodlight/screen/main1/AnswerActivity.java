package com.example.moodlight.screen.main1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.example.moodlight.R;
import com.example.moodlight.databinding.ActivityAnswerBinding;
import com.example.moodlight.util.FirebaseUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.example.moodlight.screen.main1.CommunityActiviy.todayMood;

public class AnswerActivity extends AppCompatActivity {

    private ActivityAnswerBinding binding;
    private FirebaseFirestore db;
    private int postNumber;
    private String nickName;
    private String todayQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        binding = DataBindingUtil.setContentView(this,R.layout.activity_answer);
        binding.setActivity(this);
        setColor();
        getNickName();

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
    public void addContents(View view){
        getPostNumber();
    }
    private void getPostNumber(){
        db.collection("post")
                .document("information")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        postNumber = Integer.parseInt(String.valueOf(documentSnapshot.get("todayPostNumber")))+1;
                        todayQuestion = (String)documentSnapshot.get("todayQuestion");
                        Log.d(TAG, "getPostNumber: 성공"+postNumber);
                        setContents();
                    }
                });
    }
    private void updatePostNumber(){
        Map<String,Object> map = new HashMap<>();
        map.put("todayPostNumber", postNumber);
        db.collection("post")
                .document("information")
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: 성공");
                    }
                });
    }
    private void setContents(){
        String time = modifyJoinTime(System.currentTimeMillis());
        String[] timeArray = time.split("\\.");
        Map<String,Object> content = new HashMap<>();
        content.put("todayQuestion",todayQuestion);
        content.put("answer",binding.answerEditText.getText().toString());
        content.put("commentAlram",binding.commentCheck.isChecked());
        content.put("likeAlram",binding.onlyCheck.isChecked());
        content.put("nickName",nickName);
        content.put("postNumber",postNumber);
        content.put("heart",0);
        content.put("comment",0);
        content.put("commentArray",Arrays.asList());
        content.put("isHeart",0);
        content.put("mood",todayMood);
        content.put(timeArray[1]+"."+(Integer.parseInt(timeArray[2])+1),todayMood);


        db.collection("post").
                document(String.valueOf(postNumber))
                .set(content).
                addOnCompleteListener(task ->  {
                    if (task.isSuccessful()){
                        Log.d(TAG, "addContents: 성공"+time);
                        updatePostNumber();
                        switch (todayMood){
                            case 101:
                                getCount("Happy");
                                break;
                            case 102:
                                getCount("Mad");
                                break;
                            case 103:
                                getCount("Sad");
                                break;
                        }
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: 실패"+e.getMessage());
                    }
                });

    }

    private void getNickName(){
        db.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        nickName = (String) documentSnapshot.get("nickname");
                    }
                });
    }

    public void finishActivity(View view){
        finish();
    }

    private void getCount(String mood){
        db.collection("post")
                .document("information")
                .get().addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    updateCount(mood, Integer.parseInt(String.valueOf(documentSnapshot.get("today"+mood+"Count"))));
        });
    }

    private void updateCount(String mood,int value){
        Map<String,Object> map = new HashMap<>();
        map.put("today"+mood+"Count",value+1);
        db.collection("post").document("information")
                .update(map)
                .addOnCompleteListener(task -> {

                });
    }

    private static String modifyJoinTime (Long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return simpleDateFormat.format(time);
    }

}