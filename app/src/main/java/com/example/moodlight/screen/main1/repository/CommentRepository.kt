package com.example.moodlight.screen.main1.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.moodlight.api.ServerClient
import com.example.moodlight.model.CommentModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentRepository {
    val _commentList = MutableLiveData<ArrayList<CommentModel?>>(ArrayList())
    var lastId = "-1"
    private val client = ServerClient.getApiService()

    fun getComment(id: Int) {
        client.getComment(ServerClient.accessToken, id,lastId)
            .enqueue(object : Callback<List<CommentModel?>> {
                override fun onResponse(
                    call: Call<List<CommentModel?>>,
                    response: Response<List<CommentModel?>>
                ) {
                    if (response.isSuccessful) {
                        _commentList.value = response.body() as ArrayList<CommentModel?>?
                        if (response.body()!!.isNotEmpty()) {
                            lastId =
                                (response.body() as ArrayList<CommentModel?>)[(response.body() as ArrayList<CommentModel?>).lastIndex]!!.id.toString()
                        }
                    }
                }

                override fun onFailure(call: Call<List<CommentModel?>>, t: Throwable) {
                }
            })
    }

    fun refresh() {
        lastId = "-1"
    }
}