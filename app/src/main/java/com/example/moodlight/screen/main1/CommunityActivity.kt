package com.example.moodlight.screen.main1

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.databinding.ActivityCommunityBinding
import com.example.moodlight.dialog.LoadingDialog
import com.example.moodlight.model.AnswerExistModel
import com.example.moodlight.screen.main1.viewmodel.CommunityFactory
import com.example.moodlight.screen.main1.viewmodel.CommunityViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CommunityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityBinding

    private var isNext = true

    private val viewModel: CommunityViewModel by lazy {
        ViewModelProvider(this, CommunityFactory(this.application))[CommunityViewModel::class.java]
    }
    private var adapter = AnswerAdapter()

    override fun onResume() {
        super.onResume()
        initRecycler()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_community)
        binding.viewModel = viewModel
        binding.activity = this
        binding.lifecycleOwner = this

        binding.communityAnswerRecycler.adapter = adapter


        val loadingDialog = LoadingDialog(this)
        loadingDialog.show()

        viewModel.answerList.observe(this) {
            adapter.setItem(it)
            adapter.notifyDataSetChanged()
            if (it.size in 0..14) {
                isNext = false
                adapter.removeLoading()
            }
        }
        viewModel.todayQuestion.observe(this) {
            if (it == null) {
                loadingDialog.dismiss()
                finish()
                Toast.makeText(this, "아직 주제가 정해지지 않았어요", Toast.LENGTH_SHORT).show()
            }
            if(it?.isNotEmpty() == true){
                loadingDialog.dismiss()
            }
        }
        onScroll()
        refresh()
    }
    private fun onScroll() {
        binding.communityAnswerRecycler.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemCount = recyclerView.adapter!!.itemCount - 1
                if (!binding.communityAnswerRecycler.canScrollVertically(1) && lastPosition == itemCount) {
                    if (isNext) {
                        adapter.removeLoading()
                        viewModel.getAnswer()
                    }
                }
            }
        })
    }

    fun access(view: View) {
        var currentTime = System.currentTimeMillis()
        val format = SimpleDateFormat("yyyy-MM-dd", Locale("ko", "KR"))

        ServerClient.getApiService().getMyAnswerExist(format.format(currentTime)).enqueue(object : Callback<AnswerExistModel> {
            override fun onResponse(
                    call: Call<AnswerExistModel>,
                    response: Response<AnswerExistModel>
            ) {
                if (response.isSuccessful) {
                    if (!response.body()!!.exist) {
                        val intent = Intent(applicationContext, AnswerActivity::class.java)
                        intent.putExtra("id", viewModel.id.value)
                        intent.putExtra("todayQuestion", viewModel.todayQuestion.value)
                        startActivity(intent)
                    }else {
                        Toast.makeText(view.context, "이미 답변을 했어요",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<AnswerExistModel>, t: Throwable) {
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