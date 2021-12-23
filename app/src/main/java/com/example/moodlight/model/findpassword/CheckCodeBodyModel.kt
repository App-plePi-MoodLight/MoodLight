package com.example.moodlight.model.findpassword

data class CheckCodeBodyModel(
    val confirmCode: String,
    val email: String,
    val password: String
)