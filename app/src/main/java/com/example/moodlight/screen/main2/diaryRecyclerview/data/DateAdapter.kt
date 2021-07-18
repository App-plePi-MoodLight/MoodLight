package com.example.moodlight.screen.main2.diaryRecyclerview.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moodlight.R
import com.example.moodlight.screen.main2.MainFragment2
import java.util.*


class  DateAdapter(val context: Context, val DataList: ArrayList<DateClass>): RecyclerView.Adapter<DateAdapter.MyViewHolder>(){
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val dateTv = itemView.findViewById<TextView>(R.id.textView)
        val recycler = itemView.findViewById<RecyclerView>(R.id.recycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_diary_main, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.dateTv.text = DataList[position].date
        holder.recycler.layoutManager = LinearLayoutManager(context)
        holder.recycler.adapter = QnAAdapter(context, DataList[position].list)
        holder.recycler.setHasFixedSize(true)
    }
    override fun getItemCount() = DataList.size

}