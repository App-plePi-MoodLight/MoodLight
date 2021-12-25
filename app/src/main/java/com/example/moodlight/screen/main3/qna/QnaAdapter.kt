package com.example.moodlight.screen.main3.qna

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moodlight.R
import com.example.moodlight.databinding.LayoutQnaItemBinding
import com.example.moodlight.model.qna.QnAModel

class QnaAdapter(val data : QnAModel) : RecyclerView.Adapter<QnaAdapter.ViewHolder>() {
    class ViewHolder(val binding : LayoutQnaItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_qna_item, parent, false)

        return ViewHolder(LayoutQnaItemBinding.bind(v))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]

        holder.binding.data = data

        val isExpand = data.isExpand
        holder.binding.childLayout.visibility = if(isExpand) View.VISIBLE else View.GONE
        holder.binding.expandBtn.rotation = if(isExpand) 90F else 270F

        holder.binding.expandBtn.setOnClickListener {
            data.isExpand = !data.isExpand
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = data.size
}