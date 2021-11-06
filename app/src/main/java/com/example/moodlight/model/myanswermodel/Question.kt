package com.example.moodlight.model.myanswermodel

import java.io.Serializable

data class Question(
    val activated: Boolean,
    val activatedDate: String,
    val contents: String,
    val id: String,
    val mood: String
) : Serializable