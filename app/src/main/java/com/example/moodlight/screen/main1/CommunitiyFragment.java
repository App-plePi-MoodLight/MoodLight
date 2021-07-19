package com.example.moodlight.screen.main1;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.example.moodlight.R;
import com.example.moodlight.databinding.FragmentCommunitiyBinding;

import static android.content.ContentValues.TAG;
import static com.example.moodlight.screen.main1.MainFragment1.todayMood;

public class CommunitiyFragment extends Fragment {

    private FragmentCommunitiyBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_communitiy,container,false);
        View view = binding.getRoot();
        binding.setFragment(this);
        setColor(view);
        return view;
    }

    private void setColor(View view){
        switch (todayMood){
            case 0: binding.answerBtn.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.btn_happy_background));
                break;
                case 1:
                    binding.answerBtn.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.btn_mad_background));
                break;
                case 2:
                    binding.answerBtn.setBackground(ContextCompat.getDrawable(view.getContext(),R.drawable.btn_sad_background));
                    break;
        }
    }
    public void intentAnswer(View view){
        Intent intent = new Intent(getContext(),AnswerActivity.class);
        startActivity(intent);
    }
}