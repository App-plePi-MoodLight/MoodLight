package com.example.moodlight.screen.main3.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FindPassWordViewModel : ViewModel(){
    var email : MutableLiveData<String> = MutableLiveData()
    var certText : MutableLiveData<String> = MutableLiveData()
    var certCheckText : MutableLiveData<String> = MutableLiveData()
    var registerCheckText : MutableLiveData<String> = MutableLiveData()
}