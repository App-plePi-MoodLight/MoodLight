package com.example.moodlight.api

import com.example.moodlight.model.*
import com.example.moodlight.model.moodcount.MoodCountModel
import com.example.moodlight.model.my_answer.MyAnswerModel
import com.example.moodlight.model.myanswermodel.MyAnswerListModel
import com.example.moodlight.model.myanswermodel.detailandcomment.AnswerCommentModel
import com.example.moodlight.model.question_response.QuestionResponseModel
import com.example.moodlight.model.setting.*
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

    @GET("auth/")
    fun getUserInfo() : Call<UserModel>

    @POST("auth/change-password")
    fun changePassword(@Body changePasswordModel: ChangePasswordModel) : Call<SuccussChangePasswordModel>

    @DELETE("user/")
    fun deleteUser() : Call<DeleteUserModel>

    @PUT("user/")
    fun updateNickName(@Body userUpdateModel: UserUpdateModel) : Call<SuccussChangePasswordModel>

    @GET("user/exist")
    fun distinctNickName(@Query("nickname") nickname: String) : Call<UserExistModel>

    @GET("answer/my")
    fun getMyAnswer(@Query("skip") skip : Int, @Query("take") take : Int) : Call<MyAnswerListModel>

    @GET("answer/my/all")
    fun getMyAllAnswer() : Call<MyAnswerListModel>

    @GET("comment/{questionId}")
    fun getMyAnswerComment(@Path("questionId")questionId : String,
                           @Query("start") skip : Int, @Query("take") take : Int) : Call<ArrayList<AnswerCommentModel>>
  
    @GET("answer/count/{activate_date}")
    fun getMoodCount(@Path("activate_date") date: String) : Call<MoodCountModel>

    @GET("question")
    fun getQuestion(@Query("date") date : String) : Call<QuestionResponseModel>

    @GET("answer/my/all")
    fun getMyAnswerAll() : Call<MyAnswerModel>

    @POST("auth/find-password")
    fun findPassword(@Body email : String) : Call<SuccessResponseModel>

    @POST("auth/confirm-find-password")
    fun confirmFindPassword(@Body confirmFindPasswordModel: ConfirmFindPasswordModel) : Call<SuccessResponseModel>

    @POST("auth/confirm-check")
    fun checkConfirm(@Body confirmCheckModel: ConfirmCheckModel) : Call<SuccessResponseModel>


}