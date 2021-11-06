package com.example.moodlight.screen.main1.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.screen.main1.PickMoodActivity

class CommunityFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CommunityViewModel::class.java)) {
            CommunityViewModel(application) as T
        }else{
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}