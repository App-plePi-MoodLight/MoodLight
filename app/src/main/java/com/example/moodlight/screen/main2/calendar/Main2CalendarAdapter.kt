package com.example.moodlight.screen.main2.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moodlight.R
import com.example.moodlight.util.DataType

class Main2CalendarAdapter (val viewModel : Main2CalendarViewModel) :
    RecyclerView.Adapter<Main2CalendarAdapter.CalendarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.column_calendar_day,
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
        private var itemCalendarDateIv : ImageView = itemView!!.findViewById(R.id.item_calendar_date_iv)

        fun bind(position: Int) {

            itemCalendarDateTv.text = viewModel.dateList[position].day

            when (viewModel.dateList[position].dayType) {

                DataType.CURRENT_DAY -> {
                    when (viewModel.dateList[position].moodType) {

                        DataType.HAPPY_MOOD -> {
                            itemCalendarDateIv.setImageResource(R.drawable.happy_background)
                            itemCalendarDateTv.setTextColor(Color.parseColor("#212121"))
                        }
                        DataType.SAD_MOOD -> {
                            itemCalendarDateIv.setBackgroundResource(R.drawable.sad_background)
                            itemCalendarDateTv.setTextColor(Color.parseColor("#ffffff"))
                        }
                        DataType.MAD_MOOD -> {
                            itemCalendarDateIv.setBackgroundResource(R.drawable.mad_background)
                            itemCalendarDateTv.setTextColor(Color.parseColor("#ffffff"))
                        }
                        else -> {
                            itemCalendarDateTv.setTextColor(Color.parseColor("#212121"))
                        }
                    }
                }
                DataType.LAST_DAY ->
                    itemCalendarDateTv.setTextColor(Color.parseColor("#bbbbbb"))

                else -> {}
            }




/*            if ( viewModel.dateList[position].day.equals(viewModel.today.toString()))
                itemCalendarDateTv.setTextColor(Color.parseColor("#0078ff"))*/

        }

    }

}