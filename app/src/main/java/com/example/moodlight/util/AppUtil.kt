package com.example.moodlight.util

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.screen.main2.calendar.CalendarHelper
import com.example.moodlight.screen.main2.calendar.Main2CalendarData
import com.example.moodlight.screen.main2.calendar.Main2CalendarViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger

class AppUtil {
    companion object {

        fun isNotConnectNetwork(context: Context): Boolean {
            val networkStatus: Int = NetworkStatus.getConnectivityStatus(context)

            if (networkStatus == NetworkStatus.TYPE_NOT_CONNECTED) {
                return true
            }
            return false
        }

        fun setSuccessAlarm(imageView: ImageView, textView: TextView, alarmText: String) {
            imageView.setImageResource(R.drawable.img_success)
            textView.setTextColor(Color.parseColor("#009900"))
            textView.text = alarmText
            if (imageView.visibility == View.INVISIBLE) {
                imageView.visibility = View.VISIBLE
                textView.visibility = View.VISIBLE
            }
        }

        fun setFailureAlarm(imageView: ImageView, textView: TextView, alarmText: String) {
            imageView.setImageResource(R.drawable.img_danger)
            textView.setTextColor(Color.parseColor("#fd3939"))
            textView.text = alarmText

            if (imageView.visibility == View.INVISIBLE) {
                imageView.visibility = View.VISIBLE
                textView.visibility = View.VISIBLE
            }
        }

        fun setInitialAlarm(imageView: ImageView, textView: TextView, alarmText: String) {
            imageView.setImageResource(R.drawable.img_carbon_information)
            textView.setTextColor(Color.parseColor("#acacac"))
            textView.text = alarmText

        }

        fun getNowDate(): String {
            val calendar = Calendar.getInstance()
            val date = calendar.time
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val today = sdf.format(date)

            return today

        }

        fun getLastDate(): String {

            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -1)
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
            return calendar.get(Calendar.MONTH) + 1
        }

        fun setBaseCalendarList(calendarViewModel: Main2CalendarViewModel): Unit {
            val calendarHelper by lazy { CalendarHelper() }

            val currentCalendar: Calendar = Calendar.getInstance()

            val df = DecimalFormat("00")
            val lastMonth: String = df.format(currentCalendar[Calendar.MONTH])

            val lastEndDay: Int =
                df.format(currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH).toLong()).toInt()

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
                val main2CalendarData: Main2CalendarData = Main2CalendarData(
                    (k + 1).toString(),
                    DataType.NONE_MOOD,
                    DataType.CURRENT_DAY
                )
                calendarViewModel.dateList.add(main2CalendarData)
            }

            for (m in 1..7 - calendarHelper.getEndDayOfWeek()) {
                calendarViewModel.dateList.add(
                    Main2CalendarData(m.toString(), DataType.NONE_MOOD, DataType.LAST_DAY)
                )
            }
        }

        fun isLeapYear(year: Int): Boolean {
            if (year % 4 == 0 && year % 100 != 0
                || year % 400 == 0
            ) {
                return true
            }

            return false
        }

        fun getToken(): Task<String> {

            return FirebaseMessaging.getInstance().token
        }
    }

//        fun pushToken() {
//            FirebaseMessaging.getInstance().token
//                .addOnCompleteListener { task: Task<String?> ->
//                    if (!task.isSuccessful) {
//                        Logger.warning("Fetching FCM registration token failed " + task.exception)
//                        return@addOnCompleteListener
//                    }
//                    val token = task.result
//                    Logger.verbose("token : $token")
//                }
//        }
}