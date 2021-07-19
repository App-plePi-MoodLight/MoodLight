package com.example.moodlight.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

class GetTime {


    companion object {
        fun getTime(productTime: Long): String? {
            val currentTime = System.currentTimeMillis()
            return if (currentTime - productTime < 60000) {
                ((currentTime - productTime) / 1000).toString() + "초가 "
            } else if (currentTime - productTime < 3600000) {
                ((currentTime - productTime) / 60000).toString() + "분이 "
            } else if (currentTime - productTime < 86400000) {
                ((currentTime - productTime) / 3600000).toString() + "시간이 "
            } else {
                ((currentTime - productTime) / 86400000).toString() + "일이 "
            }

        }

        @SuppressLint("SimpleDateFormat")
        fun modifyJoinTime(productTime: Long) : String? {
            val simpleDateFormat : SimpleDateFormat = SimpleDateFormat("yyyy.MM.dd")

            return simpleDateFormat.format(productTime)
        }
    }
}