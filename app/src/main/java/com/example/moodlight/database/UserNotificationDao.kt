package com.example.moodlight.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserNotificationDao {
    @Query("SELECT * FROM UserNotificationData")
    fun getAllNotificationData() : UserNotificationData

    @Insert
    fun insertUserNotificationData(userNotificationData : UserNotificationData)

    @Update
    fun changeUserNotificationData(userNotificationData : UserNotificationData)
}