package com.example.moodlight.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserData(
    @PrimaryKey
    var id : String,
    var password : String
)
