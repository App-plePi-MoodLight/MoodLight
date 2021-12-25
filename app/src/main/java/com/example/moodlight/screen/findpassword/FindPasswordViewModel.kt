package com.example.moodlight.screen.findpassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FindPasswordViewModel() : ViewModel() {
    var email : MutableLiveData<String> = MutableLiveData("")
    var confirmNum : MutableLiveData<String> = MutableLiveData("")
    var isConfirm : Boolean = false
    var password : MutableLiveData<String> = MutableLiveData("")
    var rePassword : MutableLiveData<String> = MutableLiveData("")
}