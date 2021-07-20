package com.example.moodlight.screen.main1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.moodlight.R;
import com.example.moodlight.databinding.ActivityCommunityBinding;

public class CommunityActiviy extends AppCompatActivity {

    public static int todayMood = 3;
    private ActivityCommunityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        if (todayMood != 3){
            View view = null;
            next(view);
        }

        binding = DataBindingUtil.setContentView(this,R.layout.activity_community);
        binding.setActivity(this);
    }

    public void next(View view){
        Intent intent = new Intent(this,CommunityMainActivity.class);
        startActivity(intent);
    }

    public void setMood(View view){
        switch (view.getId()){
            case R.id.happy:
                todayMood = 0;
                break;
            case R.id.mad:
                todayMood = 1;
                break;
            case R.id.sad:
                todayMood = 2;
                break;
        }
    }
}
