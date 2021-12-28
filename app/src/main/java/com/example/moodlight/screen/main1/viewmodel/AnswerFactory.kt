package com.example.moodlight.screen.main1.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.screen.main1.PickMoodActivity

class AnswerFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AnswerViewModel::class.java)) {
            AnswerViewModel(application) as T
        }else{
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}