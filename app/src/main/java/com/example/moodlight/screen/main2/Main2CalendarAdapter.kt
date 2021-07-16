package com.example.moodlight.screen.main2

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moodlight.R
import com.example.moodlight.util.DataType
import kotlin.collections.ArrayList

class Main2CalendarAdapter (val viewModel : Main2CalendarViewModel) :
    RecyclerView.Adapter<Main2CalendarAdapter.CalendarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_calendar_day,
            parent, false)

        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {

        holder.bind(position)

    }

    override fun getItemCount(): Int {
        return viewModel.dateList.size
    }


    inner class CalendarViewHolder(itemView : View?) : RecyclerView.ViewHolder(itemView!!) {

        private var itemCalendarDateTv : TextView = itemView!!.findViewById(R.id.item_calendar_date_tv)

        fun bind(position: Int) {

            itemCalendarDateTv.text = viewModel.dateList[position].day
            when (viewModel.dateList[position].moodType) {

                DataType.HAPPY_MOOD ->
                    itemCalendarDateTv.setBackgroundResource(R.drawable.text_happy_background)

                DataType.SAD_MOOD ->
                    itemCalendarDateTv.setBackgroundResource(R.drawable.text_sad_background)

                DataType.MAD_MOOD ->
                    itemCalendarDateTv.setBackgroundResource(R.drawable.text_mad_background)

                else -> {}
            }

            when (viewModel.dateList[position].dayType) {

                DataType.CURRENT_DAY ->
                    itemCalendarDateTv.setTextColor(Color.parseColor("#212121"))

                DataType.LAST_DAY ->
                    itemCalendarDateTv.setTextColor(Color.parseColor("#bbbbbb"))

                else -> {}
            }

        }

    }

}