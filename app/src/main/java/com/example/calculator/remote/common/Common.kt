package com.example.calculator.remote.common

import com.example.calculator.remote.retrofit.RetrofitClient
import com.example.calculator.remote.retrofit.RetrofitService

object Common {
    private val BASE_URL = "https://68eb-80-249-81-117.eu.ngrok.io/"
    val retrofitService: RetrofitService
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitService::class.java)
}