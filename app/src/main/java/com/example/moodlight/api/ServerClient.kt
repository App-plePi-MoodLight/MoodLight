package com.example.moodlight.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServerClient {

    // use : ServerClient.getApiService().FunctionToUse

    private const val baseUrl = "https://md.khjcode.com/"
    private var instance : Retrofit? = null

    fun getApiService() : ApiService = getInstance().create(ApiService::class.java)

    private fun getInstance() : Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return instance!!
    }

}