package com.example.moodlight.screen.main1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

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
import static com.example.moodlight.util.DataTypeJava.HAPPY_MOOD;
import static com.example.moodlight.util.DataTypeJava.MAD_MOOD;
import static com.example.moodlight.util.DataTypeJava.SAD_MOOD;

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
            case 101:
                binding.answerBtn.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_happy_background));
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
        if (binding.answerEditText.getText().toString().isEmpty()){
            Toast.makeText(this, "답변을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
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
        Map<String,Object> content = new HashMap<>();
        content.put("todayQuestion",todayQuestion);
        content.put("answer",binding.answerEditText.getText().toString());
        content.put("commentAlram",binding.commentSwitch.isChecked());
        content.put("likeAlram",binding.onlySwitch.isChecked());
        content.put("nickName",nickName);
        content.put("postNumber",postNumber);
        content.put("heart",0);
        content.put("comment",0);
        content.put("commentArray",Arrays.asList());
        content.put("mood",todayMood);

        db.collection("post").
                document(String.valueOf(postNumber))
                .set(content).
                addOnCompleteListener(task ->  {
                    if (task.isSuccessful()){
                        updatePostNumber();
                        switch (todayMood){
                            case HAPPY_MOOD:
                                getCount("Happy");
                                break;
                            case MAD_MOOD:
                                getCount("Mad");
                                break;
                            case SAD_MOOD:
                                getCount("Sad");
                                break;
                        }
                        getWritePostMap();
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

    private void getWritePostMap(){
        db.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(task -> {
                   if (!task.isSuccessful()){
                       return;
                   }
                   DocumentSnapshot documentSnapshot = task.getResult();
                   Map<String, Object> map =  documentSnapshot.getData();
                   updateWritePostMap(map);
                });
    }

    private void updateWritePostMap(Map<String, Object> map){
        Map<String,String> writePostMap = (Map<String, String>) map.get("writePostMap");

        if (map.get("writePostMap")==null){
            writePostMap = new HashMap<>();
        }
        writePostMap.put(modifyJoinTime(System.currentTimeMillis()), String.valueOf(todayMood));
        db.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("writePostMap",writePostMap);
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