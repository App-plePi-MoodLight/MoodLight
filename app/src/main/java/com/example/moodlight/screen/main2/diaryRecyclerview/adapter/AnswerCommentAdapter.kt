package com.example.moodlight.screen.main2.diaryRecyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moodlight.R
import com.example.moodlight.model.myanswermodel.detailandcomment.AnswerCommentModel

class AnswerCommentAdapter(val data : ArrayList<AnswerCommentModel?>):
    RecyclerView.Adapter<AnswerCommentAdapter.MyViewModel>() {
    class MyViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentContent = itemView.findViewById<TextView>(R.id.commentContentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewModel {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_answer_comment, parent, false)

        return MyViewModel(v)
    }

    override fun onBindViewHolder(holder: MyViewModel, position: Int) {
        holder.commentContent.text = data[position]!!.contents
    }

    override fun getItemCount(): Int  = data.size

}