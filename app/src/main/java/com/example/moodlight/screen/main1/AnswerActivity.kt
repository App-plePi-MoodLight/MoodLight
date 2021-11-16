package com.example.moodlight.screen.main1

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.moodlight.R
import com.example.moodlight.api.ServerClient
import com.example.moodlight.databinding.ActivityAnswerBinding
import com.example.moodlight.model.AnswerPostModel
import com.example.moodlight.screen.main1.viewmodel.AnswerFactory
import com.example.moodlight.screen.main1.viewmodel.AnswerViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnswerBinding

    private val viewModel: AnswerViewModel by lazy {
        ViewModelProvider(this, AnswerFactory(this.application))[AnswerViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_answer)
        binding.viewModel = viewModel
        binding.activity = this
        binding.lifecycleOwner = this

        viewModel.todayQuestion.value = intent.getStringExtra("todayQuestion")
    }

    fun postAnswer(view: View) {
        val model = AnswerPostModel(
            viewModel.answer.value!!,
            viewModel.privateChecked.value!!,
            intent.getStringExtra("id")!!
        )
        ServerClient.getApiService().postAnswer(model).enqueue(object : Callback<AnswerPostModel> {
            override fun onResponse(
                call: Call<AnswerPostModel>,
                response: Response<AnswerPostModel>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(applicationContext, "업로드에 실패하였습니다", Toast.LENGTH_SHORT).show()
                    finish()
                    return
                }
                finish()
            }

            override fun onFailure(call: Call<AnswerPostModel>, t: Throwable) {
                Toast.makeText(applicationContext, "업로드에 실패하였습니다", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

}
