package com.example.moodlight.screen.main2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moodlight.screen.main2.diaryRecyclerview.data.QnAData

class Main2ViewModel : ViewModel() {
    var month: MutableLiveData<String> = MutableLiveData("")
    var data : MutableLiveData<ArrayList<QnAData>> = MutableLiveData()
}