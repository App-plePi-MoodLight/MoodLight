package com.example.moodlight.screen.main1

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.databinding.ActivityCommentBinding
import com.example.moodlight.model.CommentPostModel
import com.example.moodlight.screen.main1.viewmodel.CommentFactory
import com.example.moodlight.screen.main1.viewmodel.CommentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentActivity : AppCompatActivity() {

    private var isNext = true
    private lateinit var binding: ActivityCommentBinding
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            CommentFactory(application, intent.getIntExtra("answerId", 0))
        )[CommentViewModel::class.java]
    }
    private var adapter = CommentAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comment)
        binding.viewModel = viewModel
        binding.activity = this

        binding.commentRecycler.adapter = adapter

        viewModel.commentList.observe(this, Observer {
            adapter.setItem(it)
            adapter.notifyDataSetChanged()
            if(it.size in 0..14){
                isNext = false
                adapter.removeLoading()
            }
        })

        onScroll()
        refresh()
    }
    private fun onScroll(){
        binding.commentRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                val itemCount = recyclerView.adapter!!.itemCount - 1

                Log.d(TAG, "lastpositon: $lastPosition")
                Log.d(TAG, "itemCount: $itemCount")
                if (!binding.commentRecycler.canScrollVertically(1) && lastPosition == itemCount) {
                    if(isNext){
                        adapter.removeLoading()
                        viewModel.getComment()
                    }
                }
            }
        })
    }

    fun postComment(view: View) {
        val commentPostModel = CommentPostModel(
            intent.getIntExtra("answerId", 0),
            viewModel.commentEdit.value!!
        )
        ServerClient.getApiService().postComment(ServerClient.accessToken, commentPostModel)
            .enqueue(object : Callback<CommentPostModel> {
                override fun onResponse(
                    call: Call<CommentPostModel>,
                    response: Response<CommentPostModel>
                ) {
                    if (!response.isSuccessful) {
                        Log.d(TAG, "onResponse: 실패 ${response.code()}")
                    }
                    initRecycler()
                    binding.commentEditText.text.clear()
                }

                override fun onFailure(call: Call<CommentPostModel>, t: Throwable) {
                }
            })
    }

    private fun refresh() {
        binding.swipeLayout.setOnRefreshListener {
            initRecycler()
            binding.swipeLayout.isRefreshing = false
        }
    }
    private fun initRecycler(){
        isNext = true
        adapter.removeAll()
        viewModel.refresh()
    }
}