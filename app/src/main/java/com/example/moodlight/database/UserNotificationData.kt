package com.example.moodlight.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class UserNotificationData (
    @PrimaryKey
    var token : String = "",
    var isCheckLikeAlarm : Boolean = null == true,
    var isCheckCommentAlarm : Boolean = null == true
)