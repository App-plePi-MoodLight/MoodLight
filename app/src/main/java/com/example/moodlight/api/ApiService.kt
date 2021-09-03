package com.example.moodlight.api

import com.example.moodlight.data.IsExistData
import com.example.moodlight.data.JoinBodyData
import com.example.moodlight.data.LoginData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("auth/join")
    fun joinRequest(@Body joinBodyData: JoinBodyData) : Call<JoinBodyData>

    @POST("auth/login")
    fun login(@Body loginData: LoginData) : Call<LoginData>

    // if exist, true. if NotExist, false
    @GET("user/exist")
    fun isExistEmail(@Query("email") email : String) : Call<IsExistData>

    @GET("user/exist")
    fun isExistNickname(@Query("nickname") nickname : String) : Call<IsExistData>

}