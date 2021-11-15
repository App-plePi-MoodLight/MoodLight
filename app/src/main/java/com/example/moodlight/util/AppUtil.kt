package com.example.moodlight.util

import com.example.moodlight.screen.main2.calendar.CalendarHelper
import com.example.moodlight.screen.main2.calendar.Main2CalendarData
import com.example.moodlight.screen.main2.calendar.Main2CalendarViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class AppUtil {
    companion object {
        fun getNowDate(): String {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, +1)
            val date = calendar.time
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val today = sdf.format(date)

            return today

        }
        fun getLastDate() : String {

            val calendar = Calendar.getInstance()
            var dDate = calendar.time
            val dSdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

            val yesterday = dSdf.format(dDate)

            return yesterday
        }
        fun getNowYear(): Int {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.YEAR)
        }
        fun getNowMonth(): Int {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.MONTH)+1
        }

        fun setBaseCalendarList(calendarViewModel : Main2CalendarViewModel) : Unit {
            val calendarHelper by lazy { CalendarHelper() }

            val currentCalendar: Calendar = Calendar.getInstance()

            val df = DecimalFormat("00")
            val lastMonth: String = df.format(currentCalendar[Calendar.MONTH])

            val lastEndDay : Int = df.format(currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH).toLong()).toInt()

            for (i in calendarHelper.getStartDayOfWeek() - 2 downTo 0) {
                calendarViewModel.dateList.add(
                    Main2CalendarData(
                        (lastEndDay - i).toString(),
                        DataType.NONE_MOOD,
                        DataType.LAST_DAY
                    )
                )
            }

            for (k in 0 until calendarHelper.getEndDay()) {
                val main2CalendarData : Main2CalendarData = Main2CalendarData(
                    (k+1).toString(),
                    DataType.NONE_MOOD,
                    DataType.CURRENT_DAY)
                calendarViewModel.dateList.add(main2CalendarData)
            }

            for (m in 1..7 - calendarHelper.getEndDayOfWeek()) {
                calendarViewModel.dateList.add(
                    Main2CalendarData(m.toString(), DataType.NONE_MOOD, DataType.LAST_DAY)
                )
            }
        }
    }
}