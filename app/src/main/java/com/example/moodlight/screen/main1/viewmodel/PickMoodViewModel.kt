package com.example.moodlight.screen.main1.viewmodel

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Application
import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moodlight.R
import com.example.moodlight.util.DataType

class PickMoodViewModel(private val application: Application) : ViewModel() {
    val buttonMood = MutableLiveData<Drawable>()
    private val isNotSelected = false


}