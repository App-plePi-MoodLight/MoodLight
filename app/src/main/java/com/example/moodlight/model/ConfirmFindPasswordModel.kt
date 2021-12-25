package com.example.moodlight.model

data class ConfirmFindPasswordModel(
    val confirmCode: String,
    val email: String,
    val password: String
)