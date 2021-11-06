package com.example.moodlight.api

import com.example.moodlight.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/join")
    fun joinRequest(@Body joinBodyModel: JoinBodyModel): Call<JoinBodyModel>

    @POST("auth/login")
    fun login(@Body loginModel: LoginModel): Call<LoginModel>

    // if exist, true. if NotExist, false
    @GET("user/exist")
    fun isExistEmail(@Query("email") email: String): Call<IsExistModel>

    @GET("user/exist")
    fun isExistNickname(@Query("nickname") nickname: String): Call<IsExistModel>

    @POST("auth/confirm")
    fun confirmRequest(@Body registerConfirmModel: RegisterConfirmModel)
            : Call<RegisterConfirmModel>

    @GET("/question")
    suspend fun getQuestion(
        @Query("date") date: String,
        @Query("mood") mood: String
    ): Response<List<QuestionModel>>

    @GET("/answer/{questionId}")
    fun getAnswer(
        @Header("accessToken") token: String?,
        @Path("questionId") questionId: String?,
        @Query("start") start: String,
        @Query("take") take: Int = 15
    ): Call<List<AnswerItemModel?>>

    @POST("/answer")
    fun postAnswer(@Body answerPostModel: AnswerPostModel): Call<AnswerPostModel>

    @PUT("/answer/like/{isLike}")
    fun recommendAnswer(
        @Path("isLike") isChecked: Boolean,
        @Body answerRecommendModel: AnswerRecommendModel
    ): Call<AnswerRecommendModel>

    @GET("/comment/{answerId}")
    fun getComment(
        @Header("accessToken") token: String?,
        @Path("answerId") id: Int,
        @Query("start") start: String,
        @Query("take") take: Int = 15
    ): Call<List<CommentModel?>>

    @POST("/comment")
    fun postComment(
        @Header("accessToken") token: String?,
        @Body commentPostModel: CommentPostModel
    ): Call<CommentPostModel>
}