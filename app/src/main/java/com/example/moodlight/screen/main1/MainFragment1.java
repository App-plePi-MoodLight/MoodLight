package com.example.moodlight.screen.main1;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moodlight.R;
import com.example.moodlight.databinding.FragmentMain1Binding;
import com.example.moodlight.screen.MainActivity;

import static android.content.ContentValues.TAG;

public class MainFragment1 extends Fragment {

    public static int todayMood;
    private FragmentMain1Binding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main1,container,false);
        binding.setFragment(this);
        View view = binding.getRoot();

        return view;
    }

    public void changeFragment(View view){
        CommunitiyFragment communitiyFragment = new CommunitiyFragment();
        MainActivity mainActivity = (MainActivity)getActivity();
        mainActivity.changeFragment(communitiyFragment);
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
