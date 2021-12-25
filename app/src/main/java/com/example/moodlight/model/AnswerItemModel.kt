package com.example.moodlight.model

import android.graphics.drawable.Drawable

data class AnswerItemModel(
    val id: Int,
    val moodLevel: Int,
    val contents: String,
    val private: Boolean,
    var likes: Int,
    val createDate: String,
    var isLike: Boolean,
    val countOfComment: Int
)