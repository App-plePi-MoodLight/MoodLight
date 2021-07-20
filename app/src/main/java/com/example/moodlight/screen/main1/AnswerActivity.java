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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import static com.example.moodlight.screen.main1.MainFragment1.todayMood;

public class AnswerActivity extends AppCompatActivity {

    private ActivityAnswerBinding binding;
    private FirebaseFirestore db;
    private int postNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        binding = DataBindingUtil.setContentView(this,R.layout.activity_answer);
        binding.setActivity(this);
        setColor();

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
    public void addContents(View view){
        getPostNumber();
    }
    public void getPostNumber(){
        db.collection("post")
                .document("information")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        DocumentSnapshot documentSnapshot = task.getResult();
                        postNumber = Integer.parseInt(String.valueOf(documentSnapshot.get("postNumber")))+1;
                        Log.d(TAG, "getPostNumber: 성공"+postNumber);
                        updatePostNumber();
                        setContents();
                    }
                });
    }
    public void updatePostNumber(){
        Map<String,Object> map = new HashMap<>();
        map.put("postNumber", postNumber);
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
    public void setContents(){
        String time = modifyJoinTime(System.currentTimeMillis());
        String[] timeArray = time.split("\\.");
        Map<String,Object> content = new HashMap<>();
        content.put("todayMood",todayMood);
        content.put("answer",binding.answerEditText.getText().toString());
        content.put("commentAlram",binding.commentCheck.isChecked());
        content.put("likeAlram",binding.onlyCheck.isChecked());
        content.put("time", Arrays.toString(timeArray));

        db.collection("post").
                document(String.valueOf(postNumber))
                .set(content).
                addOnCompleteListener(task ->  {
                    if (task.isSuccessful()){
                        Log.d(TAG, "addContents: 성공"+time);
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
    private static String modifyJoinTime (Long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return simpleDateFormat.format(time);
    }

}