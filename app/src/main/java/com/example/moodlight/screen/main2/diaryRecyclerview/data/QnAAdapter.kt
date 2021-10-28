package com.example.moodlight.screen.main2.diaryRecyclerview.data

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moodlight.R
import com.example.moodlight.model.myanswermodel.MyAnswerListModelItem
import java.util.*

class  QnAAdapter(val context : Context, val DataList: List<MyAnswerListModelItem>, val onClick: (data : MyAnswerListModelItem)-> Unit): RecyclerView.Adapter<QnAAdapter.MyViewHolder>(){
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        //ex)val 변수명 = itemView.findViewById<xml이름>(아이디네임)
        val Qcontents = itemView.findViewById<TextView>(R.id.Qcontents)
        val AcontentTv = itemView.findViewById<TextView>(R.id.AcontentsTv)
        val colorMoodView = itemView.findViewById<View>(R.id.colorMoodView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_diary_sub, parent, false)
        val myViewHolder = MyViewHolder(view)
        view.setOnClickListener {
            onClick(DataList[myViewHolder.adapterPosition])
        }
        return myViewHolder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //ex)holder.(홀더클래스변수).text = DataList[position].name
        holder.Qcontents.text = DataList[position].question.contents
        holder.AcontentTv.text = DataList[position].contents
        var colorParser = ""
        when(DataList[position].question.mood){
            "sad" ->{
                colorParser = "#10699e"
            }
            "happy"->{
                colorParser = "#f5cf66"
            }
            "angry"->{
                colorParser = "#ed5d4c"
            }
        }
        holder.colorMoodView.setBackgroundColor(Color.parseColor(colorParser))
    }
    override fun getItemCount() = DataList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }
}