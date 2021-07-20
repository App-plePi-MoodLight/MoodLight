package com.example.moodlight.mainstatics

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.moodlight.R
import com.example.moodlight.databinding.FragmentMainStatisticsBinding

class MainStatisticsFragment : Fragment() {

    private lateinit var binding : FragmentMainStatisticsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_statistics, container, false)

        (binding.wholeLayout as ViewGroup).layoutTransition.apply {
            val appearingAnimator = ObjectAnimator.ofFloat(view, "translationX", -1000f, 0f)
            val disappearingAnimator = ObjectAnimator.ofFloat(view, "translationX", 0f, 1000f)
            this.setAnimator(LayoutTransition.APPEARING, appearingAnimator)
            this.setAnimator(LayoutTransition.DISAPPEARING, disappearingAnimator)
            this.setStartDelay(LayoutTransition.APPEARING, 2000L)
            this.setDuration(LayoutTransition.APPEARING, 700L)
        }


        binding.happySum = "100"
        binding.madSum = "121"
        binding.sadSum = "99"
        binding.lastHappySum = "33"
        binding.lastMadSum = "35"
        binding.lastSadSum = "78"
        binding.lastQuestion = "오늘 하루 있었던 일 중 가장 인상깊었던 일은 무엇인가요?"
        binding.todayQuestion = "오늘의 기분은 어떤가요?"
        binding.lastStatsText1 = "어제는 분노의 감정을 가지고 있는 사람들이 많았네요"
        binding.lastStatsText2 = "분노 날려버리고 행복한 일만 가득하길 바랄게요"

        setAnimation()

        return binding.root
    }

    fun setAnimation() : Unit {
        binding.mainStatsTv1.postDelayed({
            binding.mainStatsTv1.isVisible = true
        }, 50L)
        binding.mainStatsTodayQuestionTv.postDelayed({
            binding.mainStatsTodayQuestionTv.isVisible = true
        }, 150L)
        binding.mainStatsTodayLayout.postDelayed({
            binding.mainStatsTodayLayout.isVisible = true
        }, 250L)
        binding.mainStatsTv2.postDelayed({
            binding.mainStatsTv2.isVisible = true
        }, 350L)
        binding.mainStatsLastQuestionTv.postDelayed({
            binding.mainStatsLastQuestionTv.isVisible = true
        }, 450L)
        binding.mainStatsLastLayout.postDelayed({
            binding.mainStatsLastLayout.isVisible = true
        }, 550L)
        binding.mainStatsLastStatsTv1.postDelayed({
            binding.mainStatsLastStatsTv1.isVisible = true
        }, 650L)
        binding.mainStatsLastStatsTv2.postDelayed({
            binding.mainStatsLastStatsTv2.isVisible = true
        }, 750L)
    }

}