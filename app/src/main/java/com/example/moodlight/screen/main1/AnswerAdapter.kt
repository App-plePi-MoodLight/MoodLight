package com.example.moodlight.screen.main1

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.databinding.ItemAnswerBinding
import com.example.moodlight.model.AnswerItemModel
import com.example.moodlight.model.AnswerRecommendModel
import com.example.moodlight.util.DataType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnswerAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var list = ArrayList<AnswerItemModel?>()

    inner class NormalViewHolder(private val binding: ItemAnswerBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun onBind(item: AnswerItemModel){
                binding.viewModel = item
                binding.answerCommentButton.setOnClickListener {
                    accessComment(item)
                }
                binding.answerRecommendButton.apply {
                    setOnClickListener {
                        item.isLike = !item.isLike
                        if (item.isLike) {
                            item.likes = item.likes + 1
                        } else {
                            item.likes = item.likes - 1
                        }
                        notifyDataSetChanged()
                        recommend(item.isLike, item)
                    }
                }
                binding.answerCommentImage.setOnClickListener {
                    accessComment(item)
                }
                binding.answerRecommendButton.background = setRecommendButton()

            }
        private fun accessComment(item: AnswerItemModel){
            val intent = Intent(binding.root.context, CommentActivity::class.java)
            intent.putExtra("answerId", item.id)
            binding.root.context.startActivity(intent)
        }
        private fun setRecommendButton(): Drawable {
            return when (DataType.MOOD) {
                DataType.HAPPY_MOOD -> ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.recommend_radio_button_happy
                )!!
                DataType.MAD_MOOD -> ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.recommend_radio_button_mad
                )!!
                DataType.SAD_MOOD -> ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.recommend_radio_button_sad
                )!!
                else -> ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.recommend_radio_button_happy
                )!!
            }
        }
        private fun recommend(isChecked: Boolean, item: AnswerItemModel) {
            ServerClient.getApiService()
                .recommendAnswer(isChecked, AnswerRecommendModel(item.id))
                .enqueue(object : Callback<AnswerRecommendModel> {
                    override fun onResponse(
                        call: Call<AnswerRecommendModel>,
                        response: Response<AnswerRecommendModel>
                    ) {
                        if (!response.isSuccessful) {
                            Log.d(TAG, "onResponse: 실패 ${response.code()}")
                            return
                        }
                        Log.d(TAG, "onResponse: 성공")
                    }

                    override fun onFailure(call: Call<AnswerRecommendModel>, t: Throwable) {
                        Log.d(TAG, "onResponse: 실패 ${t.message}")
                    }
                })
        }
        }



    class LoadingViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemAnswerBinding.inflate(
            LayoutInflater.from(parent.context),
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

    override fun getItemViewType(position: Int): Int {
        return if (list[position] == null) {
            DataType.LOADING_VIEW_TYPE
        } else {
            DataType.NORMAL_VIEW_TYPE
        }}

    fun setItem(list: ArrayList<AnswerItemModel?>) {
        this.list.addAll(list)
        this.list.add(null)
    }

    fun removeAll() {
        this.list.clear()
        notifyDataSetChanged()
    }

    fun removeLoading() {
        if(list.isNotEmpty()){
            list.removeAt(list.lastIndex)
        }
    }

}