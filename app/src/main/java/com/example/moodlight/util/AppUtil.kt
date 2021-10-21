package com.example.moodlight.util

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AppUtil {
    companion object {
        fun getNowDate(): String {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd")
            return current.format(formatter)
        }
        fun getLastDate() : String {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val timeToDate = calendar.time
            val formatter = SimpleDateFormat("yyyy-mm-dd")

            return formatter.format(timeToDate)
        }
    }
}