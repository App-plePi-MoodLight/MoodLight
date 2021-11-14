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
    val happyButton = MutableLiveData<Int>()
    val madButton = MutableLiveData<Drawable>()
    val madButton = MutableLiveData<Drawable>()
    val buttonMood = MutableLiveData<Drawable>()
    private val isNotSelected = false

    init {
        setMood(DataType.NONE_MOOD)
    }

    fun setMood(moodType: Int) {
        DataType.MOOD = moodType
        buttonMood.value = setButtonMood(application.applicationContext)
    }


    private fun setButtonMood(context: Context) {
        when (DataType.MOOD) {
            DataType.NONE_MOOD -> ContextCompat.getDrawable(
                context, R.drawable.btn_none_background2
            )

            DataType.HAPPY_MOOD -> {
                ValueAnimator.ofObject(ArgbEvaluator()).apply {
                    duration = 250
                    addUpdateListener {
                        happyButton.value = Color.parseColor()
                            ColorDrawable(it.animatedValue as Int)
                    }
                }.start()
            }
            DataType.MAD_MOOD -> ContextCompat.getDrawable(
                context,
                R.drawable.btn_mad_background2
            )

            DataType.SAD_MOOD -> ContextCompat.getDrawable(
                context,
                R.drawable.btn_sad_background2
            )

            else -> ContextCompat.getDrawable(
                context, R.drawable.btn_none_background2
            )
        }
    }

}