package com.example.moodlight.model

data class QuestionModel(
    val activated: Boolean,
    val activated_date: String,
    val contents: String,
    val id: String,
    val mood: String)