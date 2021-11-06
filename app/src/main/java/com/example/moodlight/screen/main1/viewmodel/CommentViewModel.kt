package com.example.moodlight.screen.main1.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moodlight.model.CommentModel
import com.example.moodlight.screen.main1.CommentAdapter
import com.example.moodlight.screen.main1.repository.CommentRepository
import com.example.moodlight.util.MoodUtilCode
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommentViewModel(application: Application,private val answerId: Int) : ViewModel() {
    private val commentRepository = CommentRepository()
    val commentEdit = MutableLiveData<String>()
    val commentButton = MutableLiveData<Drawable>()
    val commentList :MutableLiveData<ArrayList<CommentModel?>>
        get() = commentRepository._commentList

    init {
        commentList.value = arrayListOf()
        commentButton.value = MoodUtilCode.setButtonMood(application.applicationContext)
    }

    fun getComment(){
        commentRepository.getComment(answerId)
    }

    fun refresh(){
        commentList.value = ArrayList()
        commentRepository.refresh()
    }
}