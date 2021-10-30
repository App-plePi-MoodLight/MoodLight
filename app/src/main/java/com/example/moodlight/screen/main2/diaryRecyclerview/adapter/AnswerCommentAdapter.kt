package com.example.moodlight.screen.main2.diaryRecyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moodlight.R
import com.example.moodlight.model.myanswermodel.detailandcomment.AnswerCommentModel

class AnswerCommentAdapter(val data : ArrayList<AnswerCommentModel?>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    class MyViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentContent = itemView.findViewById<TextView>(R.id.commentContentTextView)
    }

    class LodingViewModel(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_ITEM->{
                val v = LayoutInflater.from(parent.context).inflate(R.layout.row_answer_comment, parent, false)
                MyViewModel(v)
            }
            else->{
                val v = LayoutInflater.from(parent.context).inflate(R.layout.row_loading, parent, false)
                LodingViewModel(v)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MyViewModel){
            holder.commentContent.text = data[position]!!.contents
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(data[position]!!.created_date){
            " "->{
                VIEW_TYPE_LOADING
            }
            else->{
                VIEW_TYPE_ITEM
            }
        }
    }

    override fun getItemCount(): Int = data.size

    fun deletLodingItem(){
        data.removeAt(data.lastIndex)
    }

}