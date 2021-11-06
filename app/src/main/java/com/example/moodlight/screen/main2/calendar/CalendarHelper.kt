package com.example.moodlight.screen.main2.calendar

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class CalendarHelper {

    private var calendar = Calendar.getInstance()
    // month 0부터 시작.

    fun plusMonth () {
        calendar.add(Calendar.MONTH, 1)
    }

    fun minusMonth () {
        calendar.add(Calendar.MONTH, -1)
    }

    fun getMonth () : Int {
        return calendar.get(Calendar.MONTH)
    }

    fun getYear () : Int {
        return calendar.get(Calendar.YEAR)
    }

    fun getDate () : Int {
        return calendar.get(Calendar.DATE)
    }

    fun getStartDayOfWeek (): Int {
        val cal : Calendar = calendar
        cal.set(Calendar.DAY_OF_MONTH, 1)
        return cal.get(Calendar.DAY_OF_WEEK)
    }

    fun getEndDay () : Int {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    fun getEndDayOfWeek () : Int {
        val cal : Calendar = calendar
        cal.set(Calendar.DAY_OF_MONTH, getEndDay())

        return cal.get(Calendar.DAY_OF_WEEK)
    }

    fun getLastEndDay(): Int {
        val cal2 :Calendar = calendar
        cal2.add(Calendar.MONTH, -1)
        return cal2.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    companion object {

        fun dateTransformationToYear(str : String) : String {
            // ex) str require :  2021-10-21T14:27:47.134Z

            val format = SimpleDateFormat("yyyy-MM-dd")
            val date: Date = format.parse(str)
            return (date.year+1900).toString()
        }

        fun dateTransformationToMonth(str : String) : String {
            // ex) str require :  2021-10-21T14:27:47.134Z

            val format = SimpleDateFormat("yyyy-MM-dd")
            val date: Date = format.parse(str)

            return (date.month+1).toString()
        }

        fun dateTransformationToDay(str : String) : String {
            // ex) str require :  2021-10-21T14:27:47.134Z
            val format = SimpleDateFormat("yyyy-MM-dd")
            val date: Date = format.parse(str)
            Log.e("nn",date.day.toString())
            return (date.day).toString()
        }

    }


}