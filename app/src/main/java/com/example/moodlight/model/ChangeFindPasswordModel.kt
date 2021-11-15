package com.example.moodlight.model

data class ChangeFindPasswordModel(
    val confirmCode: String,
    val email: String,
    val password: String
)