package com.example.moodlight.screen.register

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moodlight.R
import kotlin.properties.Delegates

class RegisterViewModel : ViewModel() {
    var email = MutableLiveData("")
    var password = MutableLiveData("")
    var nickname = MutableLiveData("")

}