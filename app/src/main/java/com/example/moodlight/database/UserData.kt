package com.example.moodlight.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userLoginTable")
data class UserData(
    @PrimaryKey
    var id : String,
    var password : String
)
