package com.example.moodlight.screen.main1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moodlight.model.AnswerItemModel

class AnswerItemViewModel(answerItemModel: AnswerItemModel){
    val answerModel = MutableLiveData<AnswerItemModel>()

    init{
        answerModel.value = answerItemModel
    }

}
