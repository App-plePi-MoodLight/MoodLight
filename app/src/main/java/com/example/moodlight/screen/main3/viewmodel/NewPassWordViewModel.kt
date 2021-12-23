package com.example.moodlight.screen.main3.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewPassWordViewModel : ViewModel() {
    var newPassWord : MutableLiveData<String> = MutableLiveData()
    var newPassWordAgain : MutableLiveData<String> = MutableLiveData()
    var dangerText : MutableLiveData<String> = MutableLiveData()
}