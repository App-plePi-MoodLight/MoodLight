package com.example.moodlight.screen.main2.calendar

import androidx.lifecycle.ViewModel
import com.example.moodlight.screen.main2.calendar.Main2CalendarData

class Main2CalendarViewModel : ViewModel() {
    var dateList : ArrayList<Main2CalendarData> = ArrayList()
    var today : Int = 0
}