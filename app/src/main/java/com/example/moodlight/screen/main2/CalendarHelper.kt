package com.example.moodlight.screen.main2

import java.util.*

class CalendarHelper {

    private lateinit var calendar : Calendar

    init {
        calendar = Calendar.getInstance()
        calendar.time = Date()
    }

    fun getStartDay (): Int {
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    fun getEndDay () : Int {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }




}