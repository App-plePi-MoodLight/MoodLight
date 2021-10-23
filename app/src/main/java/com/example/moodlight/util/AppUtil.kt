package com.example.moodlight.util

import java.text.SimpleDateFormat
import java.util.*

class AppUtil {
    companion object {
        fun getNowDate(): String {
            val now =  System.currentTimeMillis()
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(now)
            return simpleDateFormat
        }
        fun getLastDate() : String {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val timeToDate = calendar.time
            val formatter = SimpleDateFormat("yyyy-MM-dd")

            return formatter.format(timeToDate)
        }
        fun getNowYear(): Int {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.YEAR)
        }
        fun getNowMonth(): Int {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.MONTH)+1
        }
    }
}