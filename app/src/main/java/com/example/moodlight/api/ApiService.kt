package com.example.moodlight.api

import com.example.moodlight.data.JoinBodyData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("auth/join")
    fun joinRequest(@Body joinBodyData: JoinBodyData) : Call<JoinBodyData>

}