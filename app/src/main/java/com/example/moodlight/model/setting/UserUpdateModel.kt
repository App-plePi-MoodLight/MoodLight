package com.example.moodlight.model.setting

data class UserUpdateModel(
    val nickname: String,
    val userId: String,
    val usePushMessageOnComment : Boolean,
    val usePushMessageOnLike : Boolean
)