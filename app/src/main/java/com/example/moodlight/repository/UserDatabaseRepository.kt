package com.example.moodlight.repository

import android.app.Application
import com.example.moodlight.database.UserDao
import com.example.moodlight.database.UserData
import com.example.moodlight.database.UserDatabase

class UserDatabaseRepository(application: Application) {
    private val userDataBase : UserDatabase = UserDatabase.getInstance(application)!!
    private val userDao : UserDao = userDataBase.userDao()

    fun insertLoginData(userData : UserData) {
        userDao.insert(userData)
    }

    fun deleteLoginData(userData: UserData) {
        userDao.delete(userData)
    }

    fun updateLoginData(userData: UserData) {
        userDao.update(userData)
    }
}