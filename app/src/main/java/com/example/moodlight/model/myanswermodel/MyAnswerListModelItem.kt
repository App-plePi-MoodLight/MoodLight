package com.example.moodlight.model.myanswermodel

import java.io.Serializable

data class MyAnswerListModelItem(
    val contents: String,
    var createdDate: String,
    val id: Int,
    val `private`: Boolean,
    val question: Question
) : Serializable