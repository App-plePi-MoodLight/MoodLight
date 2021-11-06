package com.example.moodlight.model

data class AnswerPostModel(
    val contents: String,
    val private: Boolean,
    val questionId: String
)