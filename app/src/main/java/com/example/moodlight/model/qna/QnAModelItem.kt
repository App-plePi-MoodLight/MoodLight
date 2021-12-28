package com.example.moodlight.model.qna

data class QnAModelItem(
    val answer: String,
    var isExpand: Boolean,
    val question: String,
    val image : Int
)