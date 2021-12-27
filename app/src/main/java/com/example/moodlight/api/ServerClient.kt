package com.example.moodlight.api

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServerClient {

    // use : ServerClient.getApiService().(FunctionToUse)

    // use : ServerClient.getApiService().(FunctionToUse)

    private const val baseUrl = "https://md.khjcode.com/"
    public var accessToken : String? = null

    fun getInstance() : Retrofit {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val interceptor: Interceptor = Interceptor { chain ->
            if (accessToken != null) {
                val request: Request = chain.request().newBuilder()
                    .addHeader("Authorization", accessToken)
                    .build()
                chain.proceed(request)
            } else chain.proceed(chain.request())
        }

        val client: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        val instance : Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return instance!!
    }

    fun getApiService() : ApiService = getInstance().create(ApiService::class.java)
}