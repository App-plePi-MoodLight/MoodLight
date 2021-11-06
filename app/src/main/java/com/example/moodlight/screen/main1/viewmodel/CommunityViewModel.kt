package com.example.moodlight.screen.main1.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.model.AnswerItemModel
import com.example.moodlight.screen.main1.AnswerAdapter
import com.example.moodlight.screen.main1.repository.CommunityRepository
import com.example.moodlight.util.DataType
import com.example.moodlight.util.MoodUtilCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CommunityViewModel(application: Application) : ViewModel() {
    val repository = CommunityRepository()
    val todayQuestion: MutableLiveData<String?>
        get() = repository._todayQuestion
    val buttonMood = MutableLiveData<Drawable>()
    val answerList: MutableLiveData<ArrayList<AnswerItemModel?>>
        get() = repository._answerList
    val id: MutableLiveData<String?>
        get() = repository.questionId

    private var lastId = 0

    init {
        id.value = null
        todayQuestion.value = ""
        buttonMood.value = MoodUtilCode.setButtonMood(application.applicationContext)
        getQuestion()
    }

    private fun getQuestion() {
        var currentTime = System.currentTimeMillis()
        val format = SimpleDateFormat("yyyy-MM-dd", Locale("ko", "KR"))
        CoroutineScope(Dispatchers.Main).launch {
            repository.getQuestion(
                format.format(currentTime),
                MoodUtilCode.moodTypeToString(DataType.MOOD)
            )
            repository.getAnswer()
        }
    }

    fun getAnswer() {
        repository.getAnswer()
    }

    fun refresh(){
        answerList.value!!.clear()
        repository.refresh()
    }
}