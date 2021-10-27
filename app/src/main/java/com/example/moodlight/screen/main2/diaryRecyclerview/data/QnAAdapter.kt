package com.example.moodlight.screen.main2.diaryRecyclerview.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moodlight.R
import java.util.*

class  QnAAdapter(val context : Context, val DataList: List<QnAData>): RecyclerView.Adapter<QnAAdapter.MyViewHolder>(){
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        //ex)val 변수명 = itemView.findViewById<xml이름>(아이디네임)
        val Qcontents = itemView.findViewById<TextView>(R.id.Qcontents)
        val AcontentTv = itemView.findViewById<TextView>(R.id.AcontentsTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_diary_sub, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //ex)holder.(홀더클래스변수).text = DataList[position].name
        holder.Qcontents.text = DataList[position].QContents
        holder.AcontentTv.text = DataList[position].AContents
    }
    override fun getItemCount() = DataList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }
}