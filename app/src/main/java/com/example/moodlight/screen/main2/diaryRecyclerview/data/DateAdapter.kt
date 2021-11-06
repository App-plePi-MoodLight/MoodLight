package com.example.moodlight.screen.main2.diaryRecyclerview.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moodlight.R
import com.example.moodlight.model.myanswermodel.MyAnswerListModelItem
import com.example.moodlight.screen.main2.MainFragment2
import java.util.*


class  DateAdapter(val context: Context, val DataList: ArrayList<DateClass>, val onClick: (data : MyAnswerListModelItem)-> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val dateTv = itemView.findViewById<TextView>(R.id.textView)
        val recycler = itemView.findViewById<RecyclerView>(R.id.recycler)
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun getItemCount() = DataList.size

    override fun getItemViewType(position: Int): Int {
        return when(DataList[position].date){
            " "->VIEW_TYPE_LOADING
            else->VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MyViewHolder){
            holder.dateTv.text = DataList[position].date
            holder.recycler.layoutManager = LinearLayoutManager(context)
            holder.recycler.adapter = QnAAdapter(context, DataList[position].list){
                    data ->
                onClick(data)
            }
            holder.recycler.setHasFixedSize(true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == VIEW_TYPE_ITEM){
            val v = LayoutInflater.from(parent.context).inflate(R.layout.row_diary_main, parent, false)
            MyViewHolder(v)
        }
        else{
            val v = LayoutInflater.from(parent.context).inflate(R.layout.row_loading, parent, false)
            LoadingViewHolder(v)
        }
    }
}