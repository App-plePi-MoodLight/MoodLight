package com.example.moodlight.model

//registerConfirm request body
data class RegisterConfirmModel(
    val email : String,
    val confirmCode : String
)
