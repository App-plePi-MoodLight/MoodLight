package com.example.moodlight.screen.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.moodlight.database.UserData
import com.example.moodlight.repository.UserDatabaseRepository

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    var email = MutableLiveData("")
    var password = MutableLiveData("")
    var nickname = MutableLiveData("")
    private val repository : UserDatabaseRepository = UserDatabaseRepository(application)

    fun insertLoginData (userData: UserData) {
        repository.insertLoginData(userData)
    }

}