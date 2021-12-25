package com.example.moodlight.screen.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.moodlight.database.UserData
import com.example.moodlight.repository.UserDatabaseRepository

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : UserDatabaseRepository = UserDatabaseRepository(application)

    fun insertLoginData (userData: UserData) {
        repository.insertLoginData(userData)
    }
}