package com.example.moodlight.screen.main1

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.moodlight.R
import com.example.moodlight.databinding.ItemCommentBinding
import com.example.moodlight.model.CommentModel
import com.example.moodlight.screen.main1.viewmodel.CommentViewModel
import com.example.moodlight.util.DataType

class CommentAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = ArrayList<CommentModel?>()

    inner class LoadingViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }

    inner class NormalViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: CommentModel) {
            binding.comment.text = item.contents
            Log.d(TAG, "onBind: ${binding.comment.text}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),comple
            parent,
            false
        )
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_loading, parent, false)
        return when (viewType) {
            DataType.NORMAL_VIEW_TYPE ->
                NormalViewHolder(binding)
            else ->
                LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NormalViewHolder) {
            holder.onBind(list[position]!!)
        }
    }

    override fun getItemCount(): Int = list.size

    fun setItem(list: ArrayList<CommentModel?>) {
        this.list.addAll(list)
        this.list.add(null)
    }

    fun removeAll() {
        this.list.clear()
        notifyDataSetChanged()
    }

    fun removeLoading() {
        if (list.isNotEmpty()) {
            list.removeAt(list.lastIndex)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (list[position] == null) {
            DataType.LOADING_VIEW_TYPE
        } else {
            DataType.NORMAL_VIEW_TYPE
        }
    }

}