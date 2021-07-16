package com.example.moodlight.screen.main2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.databinding.FragmentMain2Binding
import com.example.moodlight.util.DataType
import com.example.moodlight.util.DateUtil
import java.util.*

class MainFragment2 : Fragment() {

    private lateinit var calendar : Calendar

    private val viewModel : Main2ViewModel by lazy {
        ViewModelProvider(this).get(Main2ViewModel::class.java)
    }

    private val calendarViewModel : Main2CalendarViewModel by lazy{
        ViewModelProvider(this).get(Main2CalendarViewModel::class.java)
    }

    private lateinit var binding : FragmentMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main2, container, false)
        binding.viewModel = viewModel

        setUi()

        calendar = Calendar.getInstance()
        calendar.time = Date()

        val calendarHelper = CalendarHelper()

        for (i in 0 until calendarHelper.getStartDay()) {

            calendarViewModel.dateList.add(
                Main2CalendarData(" ", DataType.NONE_MOOD, DataType.LAST_DAY)
            )
        }

        for (j in 1 until 32) {
            calendarViewModel.dateList.add(
                Main2CalendarData(j.toString(), DataType.NONE_MOOD, DataType.CURRENT_DAY)
            )
        }
/*
        for (k in 0 until calendarHelper.getEndDay()) {
            calendarViewModel.dateList.add(
                Main2CalendarData(" ", DataType.NONE_MOOD, DataType.LAST_DAY)
            )
        }*/

        val adapter : Main2CalendarAdapter = Main2CalendarAdapter(calendarViewModel)
        binding.main2CalendarRecyclerView.adapter = adapter
        binding.main2CalendarRecyclerView.adapter!!.notifyDataSetChanged()


        return binding.root
    }

    private fun setUi () {

        when (DateUtil.getMonth()+1) {
            1 -> viewModel.month = "January"
            2 -> viewModel.month = "February"
            3 -> viewModel.month = "March"
            4 -> viewModel.month = "April"
            5 -> viewModel.month = "May"
            6 -> viewModel.month = "June"
            7 -> viewModel.month = "July"
            8 -> viewModel.month = "August"
            9 -> viewModel.month = "September"
            10 -> viewModel.month = "October"
            11 -> viewModel.month = "November"
            12 -> viewModel.month = "December"
            else -> viewModel.month ="error"
        }

    }

}