package com.example.moodlight.screen.main2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moodlight.model.myanswermodel.MyAnswerListModel
import com.example.moodlight.screen.main2.diaryRecyclerview.data.DateClass
import com.example.moodlight.screen.main2.diaryRecyclerview.data.QnAData

class Main2ViewModel : ViewModel() {
    var month: MutableLiveData<String> = MutableLiveData("")
    var data : MutableLiveData<ArrayList<QnAData>> = MutableLiveData()
    var list : MutableLiveData<ArrayList<DateClass>> = MutableLiveData(ArrayList())
}