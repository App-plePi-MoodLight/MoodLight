package com.example.moodlight.screen.main1

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ViewAnimator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.databinding.ActivityPcikMoodBinding
import com.example.moodlight.screen.main1.viewmodel.PickMoodFactory
import com.example.moodlight.screen.main1.viewmodel.PickMoodViewModel
import com.example.moodlight.util.DataType

class PickMoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPcikMoodBinding
    private val viewModel: PickMoodViewModel by lazy {
        ViewModelProvider(this, PickMoodFactory(this.application))[PickMoodViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pcik_mood)
        binding.activity = this
        binding.lifecycleOwner = this
        DataType.MOOD = DataType.HAPPY_MOOD
    }

    fun access(view: View){
        startActivity(Intent(this, CommunityActivity::class.java))
    }

    fun clickMoodButton(mood: Int){
        if(mood != DataType.MOOD){
            Log.d(TAG, "clickMoodButton: ${DataType.MOOD}")
            initMood()
            DataType.MOOD = mood
            pickMood(mood)
        }
    }

    private fun pickMood(mood: Int){
        val before = ContextCompat.getColor(this, R.color.none_color)
        when(mood){
            DataType.HAPPY_MOOD -> {
                val current = ContextCompat.getColor(this, R.color.happy_color)
                ValueAnimator.ofObject(ArgbEvaluator(), before, current).apply {
                    addUpdateListener {
                        binding.pickMoodHappyBackground.setImageDrawable(ColorDrawable(it.animatedValue as Int))
                    }
                }.start()
            }
            DataType.MAD_MOOD -> {
                val current = ContextCompat.getColor(this, R.color.mad_color)
                ValueAnimator.ofObject(ArgbEvaluator(), before, current).apply {
                    addUpdateListener {
                        binding.pickMoodMadBackground.setImageDrawable(ColorDrawable(it.animatedValue as Int))
                    }
                }.start()
            }
            DataType.SAD_MOOD -> {
                val current = ContextCompat.getColor(this, R.color.sad_color)
                ValueAnimator.ofObject(ArgbEvaluator(), before, current).apply {
                    addUpdateListener {
                        binding.pickMoodSadBackground.setImageDrawable(ColorDrawable(it.animatedValue as Int))
                    }
                }.start()
            }
        }
    }

    private fun initMood(){
        val current = ContextCompat.getColor(this, R.color.none_color)
        when(DataType.MOOD){
            DataType.HAPPY_MOOD -> {
                val before = ContextCompat.getColor(this, R.color.happy_color)
                ValueAnimator.ofObject(ArgbEvaluator(), before, current).apply {
                    addUpdateListener {
                        binding.pickMoodHappyBackground.setImageDrawable(ColorDrawable(it.animatedValue as Int))
                    }
                }.start()
            }
            DataType.MAD_MOOD -> {
                val before =  ContextCompat.getColor(this,R.color.mad_color)
                ValueAnimator.ofObject(ArgbEvaluator(), before, current).apply {
                    addUpdateListener {
                        binding.pickMoodMadBackground.setImageDrawable(ColorDrawable(it.animatedValue as Int))
                    }
                }.start()
            }
            DataType.SAD_MOOD -> {
                val before = ContextCompat.getColor(this,R.color.sad_color)
                ValueAnimator.ofObject(ArgbEvaluator(), before, current).apply {
                    addUpdateListener {
                        binding.pickMoodSadBackground.setImageDrawable(ColorDrawable(it.animatedValue as Int))
                    }
                }.start()
            }
        }
    }
}