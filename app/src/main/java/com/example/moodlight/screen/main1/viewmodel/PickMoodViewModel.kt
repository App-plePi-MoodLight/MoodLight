package com.example.moodlight.screen.main1.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moodlight.R
import com.example.moodlight.util.DataType

class PickMoodViewModel(private val application: Application) : ViewModel() {
    val buttonMood = MutableLiveData<Drawable>()

    init {
        setMood(DataType.NONE_MOOD)
    }

    fun setMood(moodType : Int){
        DataType.MOOD = moodType
        buttonMood.value = setButtonMood(application.applicationContext)
    }


    fun setButtonMood(context: Context): Drawable? {
        return when (DataType.MOOD) {
            DataType.NONE_MOOD -> ContextCompat.getDrawable(
                context, R.drawable.btn_none_background2
            )

            DataType.HAPPY_MOOD -> ContextCompat.getDrawable(
                context,
                R.drawable.btn_happy_background2
            )

            DataType.MAD_MOOD -> ContextCompat.getDrawable(
                context,
                R.drawable.btn_mad_background2
            )

            DataType.SAD_MOOD -> ContextCompat.getDrawable(
                context,
                R.drawable.btn_sad_background2
            )

            else -> ContextCompat.getDrawable(
                context, R.drawable.btn_none_background2)
        }
    }

}