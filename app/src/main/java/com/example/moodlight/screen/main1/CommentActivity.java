package com.example.moodlight.screen.main1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.moodlight.R;
import com.example.moodlight.databinding.ActivityCommentBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class CommentActivity extends AppCompatActivity {

    private int postNumber;

    private FirebaseFirestore db;
    private ActivityCommentBinding binding;
    private ArrayList<String> list;
    private CommentAdapter adapter;
    private DocumentSnapshot lastVisibleDocument;
    private CommunityItem item;

    @Override
    protected void onStart() {
        super.onStart();
        setComment(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        lastVisibleDocument = null;
        Intent intent = getIntent();
        item = (CommunityItem) intent.getSerializableExtra("comment");
        postNumber = intent.getIntExtra("postNumber",0);

        db = FirebaseFirestore.getInstance();
        binding = DataBindingUtil.setContentView(this,R.layout.activity_comment);
        binding.setActivity(this);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        binding.commentList.setLayoutManager(manager);

        list = new ArrayList<>();
        adapter = new CommentAdapter(list);
        binding.commentList.setAdapter(adapter);

        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                setComment(item);
                binding.swipeLayout.setRefreshing(false);
            }
        });

    }
    public void submitComment(View view){
        list.add(binding.commentEditText.getText().toString());
        adapter.notifyDataSetChanged();
        binding.commentEditText.setText(null);
        db.collection("post")
                .document(String.valueOf(postNumber))
                .update("commentArray",list)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()){
                        Toast.makeText(this, "업로드에 실패하였습니다", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setCommentCount();
                });
    }
    private void setComment(CommunityItem item){
        for(int i = 0; i < item.getCommentArray().size();i++){
            list.add(item.getCommentArray().get(i));
        }
        adapter.notifyDataSetChanged();
    }
    public void setCommentCount(){
        Map<String,Object> map = new HashMap<>();
        map.put("comment",list.size());
        db.collection("post")
                .document(String.valueOf(item.getPostNumber()))
                .update(map);
    }

    public void finishActivity(View view){
        finish();
    }
}
