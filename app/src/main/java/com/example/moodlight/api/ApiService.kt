package com.example.moodlight.api

import com.example.moodlight.model.IsExistModel
import com.example.moodlight.model.JoinBodyModel
import com.example.moodlight.model.LoginModel
import com.example.moodlight.model.RegisterConfirmModel
import com.example.moodlight.model.myanswermodel.MyAnswerListModel
import com.example.moodlight.model.myanswermodel.detailandcomment.AnswerCommentModel
import com.example.moodlight.model.setting.*
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

    @GET("comment/{questionId}")
    fun getMyAnswerComment(@Path("questionId")questionId : String,
                           @Query("skip") skip : Int, @Query("take") take : Int) : Call<ArrayList<AnswerCommentModel>>
  
    @GET("answer/count/{activate_date}")
    fun getMoodCount(@Path("activate_date") date: String) : Call<MoodCountModel>

    @GET("question")
    fun getQuestion(@Query("date") date : String) : Call<QuestionResponseModel>

    @GET("answer/my/all")
    fun getMyAnswerAll() : Call<MyAnswerModel>
}