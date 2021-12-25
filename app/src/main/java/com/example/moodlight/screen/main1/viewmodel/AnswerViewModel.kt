package com.example.moodlight.screen.main1.viewmodel

import android.app.Application
import android.graphics.drawable.Drawable
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moodlight.util.MoodUtilCode

class AnswerViewModel(application: Application) : ViewModel() {
    val moodLevel = MutableLiveData<Int>()
    val levelText = MutableLiveData<String>()
    val todayQuestion = MutableLiveData<String>()
    val answer = MutableLiveData<String>()
    val buttonMood = MutableLiveData<Drawable>()
    val privateChecked = MutableLiveData<Boolean>()

    init {
        moodLevel.value = 0
        buttonMood.value = MoodUtilCode.setButtonMood(application.applicationContext)
        levelText.value = MoodUtilCode.setLevelText()
        privateChecked.value = false
    }

    fun moodLevelUp(view: View){
        if(moodLevel.value != 10){
            moodLevel.value = moodLevel.value!! + 1
        }
    }

    fun moodLevelDown(view : View){
        if(moodLevel.value != 0){
            moodLevel.value = moodLevel.value!! - 1
        }
    }
}