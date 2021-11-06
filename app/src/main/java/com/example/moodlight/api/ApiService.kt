package com.example.moodlight.api

import com.example.moodlight.model.IsExistModel
import com.example.moodlight.model.JoinBodyModel
import com.example.moodlight.model.LoginModel
import com.example.moodlight.model.RegisterConfirmModel
import com.example.moodlight.model.moodcount.MoodCountModel
import com.example.moodlight.model.my_answer.MyAnswerModel
import com.example.moodlight.model.question_response.QuestionResponseModel
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

    @GET("question")
    fun getQuestion(@Query("date") date : String) : Call<QuestionResponseModel>

    @GET("answer/my")
    fun getMyAnswer() : Call<MyAnswerModel>

    @GET("answer/my/all")
    fun getMyAnswerAll() : Call<MyAnswerModel>
}