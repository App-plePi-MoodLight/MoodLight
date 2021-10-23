package com.example.moodlight.model.question_response

data class QuestionResponseModelItem(
    val activated: Boolean,
    val activatedDate: String,
    val contents: String,
    val id: String,
    val mood: String
)