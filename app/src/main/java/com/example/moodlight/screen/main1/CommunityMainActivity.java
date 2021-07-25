package com.example.moodlight.screen.main1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.moodlight.R;
import com.example.moodlight.databinding.ActivityCommunityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.example.moodlight.screen.main1.CommunityActiviy.todayMood;
import static com.example.moodlight.util.DataTypeJava.HAPPY_MOOD;
import static com.example.moodlight.util.DataTypeJava.MAD_MOOD;
import static com.example.moodlight.util.DataTypeJava.SAD_MOOD;

public class CommunityMainActivity extends AppCompatActivity {
    private ActivityCommunityMainBinding binding;
    private FirebaseFirestore db;
    private boolean isLoding = false;
    private boolean todayPost = true;

    private ArrayList<CommunityItem> list;
    private CommunityAdapter adapter;
    private DocumentSnapshot lastVisibleDocument;

    @Override
    protected void onResume() {
        super.onResume();
        getTodayPost();
        setlist();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_main);

        lastVisibleDocument = null;

        db = FirebaseFirestore.getInstance();

        binding = DataBindingUtil.setContentView(this,R.layout.activity_community_main);
        binding.setActivity(this);
        setColor();

        getQuestion();
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        binding.answerList.setLayoutManager(manager);

        list = new ArrayList<>();
        adapter = new CommunityAdapter(list);
        binding.answerList.setAdapter(adapter);

        binding.answerList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoding&&lastVisibleDocument != null){
                    if (manager != null && manager.findLastCompletelyVisibleItemPosition() == list.size()-1){
                        isLoding = true;
                        setlist();
                    }
                }
            }
        });

        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                adapter.notifyDataSetChanged();
                lastVisibleDocument = null;
                setlist();
                binding.swipeLayout.setRefreshing(false);
            }
        });


    }
    private void setColor(){
        switch (todayMood){
            case HAPPY_MOOD: binding.answerBtn.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_happy_background));
                break;
            case MAD_MOOD:
                binding.answerBtn.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_mad_background));
                break;
            case SAD_MOOD:
                binding.answerBtn.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_sad_background));
                break;
        }
    }
    public void intentAnswer(View view){
        if (!todayPost){
            Intent intent = new Intent(this,AnswerActivity.class);
            startActivity(intent);
            return;
        }
        Toast.makeText(this, "이미 답변을 하였습니다.", Toast.LENGTH_SHORT).show();
    }

    private Query setAnswer(){
        CollectionReference collectionReference = db.collection("post");
        if (lastVisibleDocument == null){
            return collectionReference.orderBy("postNumber",
                    Query.Direction.DESCENDING).whereEqualTo("mood",todayMood).limit(20);
        }
        return collectionReference.whereEqualTo("mood",todayMood).orderBy("postNumber",
                Query.Direction.DESCENDING).startAfter(lastVisibleDocument).limit(20);
    }
    private void setlist(){
        list.add(null);
        adapter.notifyItemInserted(list.size()-1);

        Query query = setAnswer();
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                list.remove(list.size()-1);
                adapter.notifyItemRemoved(list.size()-1);
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
                Log.d(TAG, "onFailure: 실패"+e.getMessage());
            }
        });
    }

    private void getQuestion(){
        db.collection("post")
                .document("information")
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        return;
                    }
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String question = String.valueOf(documentSnapshot.get("todayQuestion"));
                    binding.todayQuestion.setText(question);
                });
    }

    private void getTodayPost(){
        db.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(task -> {
                   if (!task.isSuccessful()){
                       return;
                   }
                   DocumentSnapshot documentSnapshot = task.getResult();
                   Map<String,Object> map = (Map<String, Object>) documentSnapshot.getData();
                   Map<String, String> writePostMap = (Map<String, String>) map.get("writePostMap");

                   if (map.get("writePostMap")==null){
                       todayPost = false;
                       return;
                   } else if (writePostMap.get(modifyJoinTime()) != null){
                       todayPost = true;
                       return;
                   }
                   todayPost = false;
                });
    }

    public void finishActvity(View view){
        todayMood = 3;
        finish();
    }

    private static String modifyJoinTime () {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String time = simpleDateFormat.format(System.currentTimeMillis());
        return time;
    }
}

