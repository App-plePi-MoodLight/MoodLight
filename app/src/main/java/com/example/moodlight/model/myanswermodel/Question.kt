package com.example.moodlight.model.myanswermodel

data class Question(
    val activated: Boolean,
    val activatedDate: String,
    val contents: String,
    val id: String,
    val mood: String
)