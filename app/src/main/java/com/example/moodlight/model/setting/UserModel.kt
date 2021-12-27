package com.example.moodlight.model.setting

data class UserModel(
    val created_date: String,
    val email: String,
    val firebaseToken: String,
    val id: String,
    val is_admin: Boolean,
    val nickname: String,
    val password: String
)