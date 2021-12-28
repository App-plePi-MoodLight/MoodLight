package com.example.moodlight.screen.main2

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.databinding.ActivityDetailAnswerBinding
import com.example.moodlight.model.myanswermodel.MyAnswerListModelItem
import com.example.moodlight.model.myanswermodel.detailandcomment.AnswerCommentModel
import com.example.moodlight.screen.main2.diaryRecyclerview.adapter.AnswerCommentAdapter
import com.example.moodlight.util.MoodColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailAnswerActivity : AppCompatActivity() {

    private var limitPage : Int = 10
    private var correntId : Int = -1
    private var isLoding : Boolean = false
    private val commentList : ArrayList<AnswerCommentModel?> = ArrayList()
    private lateinit var binding: ActivityDetailAnswerBinding
    private lateinit var intentData : MyAnswerListModelItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_answer)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_answer)
        intentData = intent.getSerializableExtra("data") as MyAnswerListModelItem

        setUi()
        lodingData()

        binding.recycler.addOnScrollListener(object :  RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


                if(!isLoding){
                    if((recyclerView.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition() == commentList.size-1){
                        lodingData()
                        this@DetailAnswerActivity.isLoding = true
                    }
                }
            }
        })

        setSupportActionBar(binding.main2Toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_btn)
    }

    private fun lodingData() {
        CoroutineScope(Dispatchers.IO).launch {
            ServerClient.getApiService().getMyAnswerComment(intentData.id.toString(),
                correntId,
                10
            )
                .enqueue(object : Callback<ArrayList<AnswerCommentModel>>{
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(
                        call: Call<ArrayList<AnswerCommentModel>>,
                        response: Response<ArrayList<AnswerCommentModel>>
                    ) {
                        val result = response.body()
                        if(response.isSuccessful){
                            Log.d(TAG, "onResponse: 댓글리스트 : $result 정보 : $response")
                            if(result!!.size!=0){
                                if(commentList.size == 0){
                                    result?.let { commentList.addAll(it) }
                                    commentList.add(AnswerCommentModel(" ", " ", 2147483647))
                                    binding.recycler.adapter = AnswerCommentAdapter(commentList)
                                }
                                else{
                                    AnswerCommentAdapter(commentList).deletLodingItem()
                                    result?.let { commentList.addAll(it) }
                                    commentList.add(AnswerCommentModel(" ", " ", 2147483647))
                                    binding.recycler.adapter!!.notifyDataSetChanged()
                                    isLoding = false
                                }
                                correntId = commentList[commentList.lastIndex-1]!!.id
                            }
                            else if(result.size == 0 && commentList.size != 0){
                                Log.d(TAG, "onResponse: resultsize : 클럭됨")
                                binding.recycler.adapter!!.notifyDataSetChanged()
                                AnswerCommentAdapter(commentList).deletLodingItem()
                            }
                        }
                        else{
                            Toast.makeText(this@DetailAnswerActivity, "댓글 리스트를 불러오는데에 실패하였습니다.2", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "onResponse: $response")
                        }
                    }

                    override fun onFailure(call: Call<ArrayList<AnswerCommentModel>>, t: Throwable) {
                        Toast.makeText(this@DetailAnswerActivity, "댓글 리스트를 불러오는데에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }

                })
        }
    }

    private fun setUi() {
        binding.QcontentTextView.text = intentData.question.contents
        binding.isDateAnswerTextView.text = setAnswerDate(intentData.createdDate)
        binding.AcontentTextView.text = intentData.contents

        when(intentData.question.mood){
            "sad"->{
                setMoodColor(MoodColor().sad)
            }
            "happy"->{
                setMoodColor(MoodColor().happy)
            }
            "angry"->{
                setMoodColor(MoodColor().angry)
            }
        }
    }

    private fun setMoodColor(color: String) {
        binding.colorMood1.setColorFilter(Color.parseColor(color))
        binding.colorMood2.setBackgroundColor(Color.parseColor(color))
        binding.colorMood3.setColorFilter(Color.parseColor(color))
    }

    private fun setAnswerDate(intentData: String) : String {
        var ogSf = SimpleDateFormat("MM'월' dd일")
        var changeSf = SimpleDateFormat("'.'MM'.'dd")
        val date = ogSf.parse(intentData)

        return Calendar.getInstance().get(Calendar.YEAR).toString().plus(changeSf.format(date))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}