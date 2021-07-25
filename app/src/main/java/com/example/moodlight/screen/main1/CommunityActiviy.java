package com.example.moodlight.screen.main1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.example.moodlight.R;
import com.example.moodlight.databinding.ActivityCommunityBinding;

import static com.example.moodlight.util.DataTypeJava.HAPPY_MOOD;
import static com.example.moodlight.util.DataTypeJava.MAD_MOOD;
import static com.example.moodlight.util.DataTypeJava.SAD_MOOD;

public class CommunityActiviy extends AppCompatActivity {

    public static int todayMood;
    private ActivityCommunityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        todayMood = 100;

        binding = DataBindingUtil.setContentView(this,R.layout.activity_community);
        binding.setActivity(this);
    }

    public void next(View view){
        if (todayMood != 100){
            Intent intent = new Intent(this,CommunityMainActivity.class);
            startActivity(intent);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("오늘의 기분을 선택해주세요")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }

    public void setMood(View view){
        switch (view.getId()){
            case R.id.happy:
                todayMood = HAPPY_MOOD;
                binding.enterbtn.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_happy_backgroun2));
                break;
            case R.id.mad:
                todayMood = MAD_MOOD;
                binding.enterbtn.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_mad_backgroun2));
                break;
            case R.id.sad:
                todayMood = SAD_MOOD;
                binding.enterbtn.setBackground(ContextCompat.getDrawable(this,R.drawable.btn_sad_backgroun2));
                break;
        }
    }
}
