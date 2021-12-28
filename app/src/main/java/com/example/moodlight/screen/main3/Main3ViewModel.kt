package com.example.moodlight.screen.main3

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Main3ViewModel() : ViewModel() {
    var email: MutableLiveData<String> = MutableLiveData("Loading")
    var username: MutableLiveData<String> = MutableLiveData("Loading")
    var main3Tv1Text: MutableLiveData<String> = MutableLiveData("Loading")
    var subscription: MutableLiveData<String> = MutableLiveData("Loading")
    var commentIsChecked: MutableLiveData<Boolean> = MutableLiveData(false)
    var likeIsChecked: MutableLiveData<Boolean> = MutableLiveData(false)

}