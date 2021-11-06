package com.example.moodlight.screen.main1.viewmodel

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moodlight.util.MoodUtilCode

class AnswerViewModel(application: Application) : ViewModel() {
    val todayQuestion = MutableLiveData<String>()
    val answer = MutableLiveData<String>()
    val buttonMood = MutableLiveData<Drawable>()
    val privateChecked = MutableLiveData<Boolean>()

    init {
        buttonMood.value = MoodUtilCode.setButtonMood(application.applicationContext)
        privateChecked.value = false
    }


}