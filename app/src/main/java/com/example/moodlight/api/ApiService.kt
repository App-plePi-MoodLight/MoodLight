package com.example.moodlight.api

import com.example.moodlight.model.IsExistModel
import com.example.moodlight.model.JoinBodyModel
import com.example.moodlight.model.LoginModel
import com.example.moodlight.model.RegisterConfirmModel
import com.example.moodlight.model.setting.ChangePasswordModel
import com.example.moodlight.model.setting.UserModel
import com.example.moodlight.model.setting.DeleteUserModel
import com.example.moodlight.model.setting.SuccussChangePasswordModel
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
}