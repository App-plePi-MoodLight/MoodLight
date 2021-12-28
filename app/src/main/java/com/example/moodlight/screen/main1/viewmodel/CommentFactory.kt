package com.example.moodlight.screen.main1.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CommentFactory(private val application: Application,private val answerId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CommentViewModel::class.java)) {
            CommentViewModel(application, answerId) as T
        }else{
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}