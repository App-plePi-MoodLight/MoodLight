package com.example.moodlight.api

import com.example.moodlight.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("auth/join")
    fun joinRequest(@Body joinBodyModel: JoinBodyModel) : Call<JoinBodyModel>

    @POST("auth/login")
    fun login(@Body loginModel: LoginModel) : Call<LoginModel>

    // if exist, true. if NotExist, false
    @GET("user/exist")
    fun isExistEmail(@Query("email") email : String) : Call<IsExistModel>

    @GET("user/exist")
    fun isExistNickname(@Query("nickname") nickname : String) : Call<IsExistModel>

    @POST("auth/confirm")
    fun confirmRequest(@Body registerConfirmModel: RegisterConfirmModel)
    : Call<RegisterConfirmModel>

    @GET("answer/count/{activate_date}")
    fun getMoodCount(@Path("activate_date") date: String) : Call<MoodCountModel>


}